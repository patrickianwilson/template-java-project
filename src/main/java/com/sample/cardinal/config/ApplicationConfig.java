package com.sample.cardinal.config;

public interface ApplicationConfig {
    String oauthAuthorizationUrl();
    String oauthTokenUrl();
    String rabbitBasePath();
    Integer port();

    String clientId();
    String redirectUri();
    String responseType();
    String scope();
}
