#!/bin/bash

export WS_URL=http://localhost:8080
export MODULE_PATH=`pwd`
mkdir -p ${MODULE_PATH}/build/tmp

if [[ ! -f ${MODULE_PATH}/build/tmp/openapi-codegen.jar ]]; then
    wget -O ${MODULE_PATH}/build/tmp/openapi-codegen.jar https://repo1.maven.org/maven2/org/openapitools/openapi-generator-cli/6.2.1/openapi-generator-cli-6.2.1.jar
fi

java -jar ${MODULE_PATH}/build/tmp/openapi-codegen.jar generate\
 --input-spec ${WS_URL}/openapi.yaml\
 --generator-name java\
 --output ${MODULE_PATH}/\
 --config ${MODULE_PATH}/config/codegen-java-config.json \
 -p enumClassPrefix=true\
 --skip-validate-spec

rm "${MODULE_PATH}/settings.gradle"
rm "${MODULE_PATH}/pom.xml"
rm "${MODULE_PATH}/.travis.yml"
rm "${MODULE_PATH}/build.sbt"
rm -r "${MODULE_PATH}/gradle/"
rm "${MODULE_PATH}/gradle.properties"
rm "${MODULE_PATH}/git_push.sh"

mkdir -p ${MODULE_PATH}/src/main/java/com/inquestdevops/%%{{ServiceModuleName}}%%/service/config

ClientFactoryContents="package com.inquestdevops.%%{{ServiceModuleName}}%%.service.config;

import com.inquestdevops.%%{{ServiceModuleName}}%%.service.ApiClient;
import com.inquestdevops.%%{{ServiceModuleName}}%%.service.api.DefaultApi;
import lombok.Getter;

import java.util.concurrent.TimeUnit;

public class ClientFactory {

    @Getter
    public enum Environments {
        LOCAL(\"http://localhost:8080\"),
        DEV(\"http://%%{{ServiceModuleName}}%%-dev.inquestdevops.com\"),
        PROD(\"http://%%{{ServiceModuleName}}%%.inquestdevops.com\");

        private String baseUrl;
        private int readTimeout = (int) TimeUnit.SECONDS.toMillis(20);
        private int connectTimeout = (int) TimeUnit.SECONDS.toMillis(10);

        Environments(String baseUrl) {
            this.baseUrl = baseUrl;
        }

    }

    public static DefaultApi getClient(Environments env, String rabbitBaseUrl, String clientId, String clientSecret) {
        DefaultApi client = new DefaultApi(
                new ApiClient().setBasePath(env.baseUrl)
                        .setReadTimeout(env.readTimeout)
                        .setConnectTimeout(env.connectTimeout)

        );

        client.getApiClient().getHttpClient().setAuthenticator(
                new RabbitServiceAccountAuthenticator(rabbitBaseUrl, clientId, clientSecret));

        return client;

    }




}
"
echo "${ClientFactoryContents}" > "${MODULE_PATH}/src/main/java/com/inquestdevops/%%{{ServiceModuleName}}%%/service/config/ClientFactory.java"


RabbitServiceAccountAuthenticatorContents="package com.inquestdevops.%%{{ServiceModuleName}}%%.service.config;

import com.google.gson.Gson;
import com.squareup.okhttp.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.Proxy;

@Slf4j
public class RabbitServiceAccountAuthenticator implements Authenticator {

    public static int MAX_REFRESH_ATTEMPTS = 3;

    private String rabbitServiceBaseUrl;
    private String clientId;
    private String clientSecret;
    private String currentToken;

    private Gson gson = new Gson();

    public RabbitServiceAccountAuthenticator(String rabbitServiceBaseUrl, String clientId, String clientSecret) {
        this.rabbitServiceBaseUrl = rabbitServiceBaseUrl;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    @Override
    public Request authenticate(Proxy proxy, Response response) throws IOException {
        log.info(\"Attempting to reauthenticate the client by getting a new Rabbit access token using service account credentials\");

        if (response.priorResponse() != null && response.priorResponse().priorResponse() != null) {
            //this has been retried twice already, something is wrong.
            log.error(\"Unable to refresh access token using provided client credentials\");
            return null;
        }
        if (currentToken == null || (response.priorResponse() != null && response.priorResponse().code() == 401)) {
            this.currentToken = refreshAccessToken(0);
        }

        return response.request().newBuilder().addHeader(\"Authorization\", \"Bearer \" + currentToken)
                .build();
    }

    protected String refreshAccessToken(int attemptNum) throws IOException {
        if (attemptNum >= MAX_REFRESH_ATTEMPTS) {
            return null;
        }
        OkHttpClient authClient = new OkHttpClient();
        authClient.setCache(null);

        Response authResp = authClient.newCall(new Request.Builder()
                .url(String.format(\"%s/token\", this.rabbitServiceBaseUrl))
                .post(RequestBody.create(MediaType.parse(\"application/x-www-form-urlencoded\"),
                        \"grant_type=client_credentials\" +
                                \"&client_id=\" + clientId +
                                \"&client_secret=\" + clientSecret))
                .build()).execute();

        if (authResp.code() == 401) {
            throw new AuthenticationException(\"Unable to authenticate for access token! Invalid Service Account Credentials! Response code=\" + authResp.code());
        }

        if (authResp.code() != 200) {
            //attempt a retry=
            try {
                Thread.sleep(200 * (2 ^ attemptNum)); //exponential backoff 200ms, 400ms, 800ms
                refreshAccessToken(attemptNum++);
            } catch (InterruptedException e) {
                Thread.interrupted();
                return null;
            }
        }

        AuthTokenResponse token = this.gson.fromJson(authResp.body().string(), AuthTokenResponse.class);
        return token.access_token;
    }

    @Override
    public Request authenticateProxy(Proxy proxy, Response response) throws IOException {
        return null;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class AuthTokenResponse {
        private String access_token;
        private String tokenType;
        private int expires_in;  //number of seconds this token is valid for.
    }

    public class AuthenticationException extends RuntimeException {
        public AuthenticationException(String s) {
            super(s);
        }
    }
}

"
echo "${RabbitServiceAccountAuthenticatorContents}" > "${MODULE_PATH}/src/main/java/com/inquestdevops/%%{{ServiceModuleName}}%%/service/config/RabbitServiceAccountAuthenticator.java"

