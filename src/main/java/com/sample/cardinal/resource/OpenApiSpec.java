package com.sample.cardinal.resource;

import com.google.inject.Inject;
import com.sample.cardinal.config.ApplicationConfig;
import io.swagger.v3.jaxrs2.integration.JaxrsOpenApiContextBuilder;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.integration.OpenApiConfigurationException;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@OpenAPIDefinition(info = @Info(
        title = "Cardinal Service API",
        description = "A template API ready to be extended!",
        version = "1"
))
public class OpenApiSpec  {

    private ApplicationConfig appConfig;

    @Inject
    public OpenApiSpec(ApplicationConfig appConfig) {
        this.appConfig = appConfig;
        initializeOpenApiSpec();
    }

    public void initializeOpenApiSpec() {
        OpenAPI openAPI = new OpenAPI();
        io.swagger.v3.oas.models.info.Info openApiInfo = new io.swagger.v3.oas.models.info.Info();
        openAPI.info(openApiInfo);

        //programmatically add auth requirements (they require configurable URLS and thus can't be annotated).
        openAPI.components(new Components()
                .addSecuritySchemes("basicAuth", getEmergencyBasicAuthSecurity())
                .addSecuritySchemes("rabbitOAuth", getRabbitOAuthSecurity()));

        SwaggerConfiguration openApiConfig = new SwaggerConfiguration()
                .openAPI(openAPI)
                .prettyPrint(true)
                .resourcePackages(
                        Stream.of(
                                //scan our controllers package.
                                StatusResource.class.getPackage().getName()
                        ).collect(Collectors.toSet()));

        try {
            new JaxrsOpenApiContextBuilder()
                    .openApiConfiguration(openApiConfig)
                    .buildContext(true);
        } catch (OpenApiConfigurationException e) {
            throw new RuntimeException("Exception while booting up Swagger Context!", e);
        }
    }
    private io.swagger.v3.oas.models.security.SecurityScheme getEmergencyBasicAuthSecurity() {
        return new io.swagger.v3.oas.models.security.SecurityScheme()
                .name("basicAuth")
                .type(io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP)
                .scheme("basic");
    }

    private io.swagger.v3.oas.models.security.SecurityScheme getRabbitOAuthSecurity() {

        return new io.swagger.v3.oas.models.security.SecurityScheme()
                .name("rabbitOAuth")
                .type(io.swagger.v3.oas.models.security.SecurityScheme.Type.OAUTH2)
                .flows(new OAuthFlows()
                        .authorizationCode(new OAuthFlow()
                                .authorizationUrl(appConfig.oauthAuthorizationUrl())
                                .tokenUrl(appConfig.oauthTokenUrl())));
    }
}
