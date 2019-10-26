package com.sample.cardinal;

import com.google.common.collect.ImmutableList;
import com.google.inject.Module;
import com.sample.cardinal.accessors.AccessorModule;
import com.sample.cardinal.config.ConfigModule;
import com.sample.cardinal.resource.StatusResource;
import com.sample.cardinal.resource.WebModule;
import com.sample.cardinal.repositories.RepositoryModule;
import com.sample.cardinal.controllers.ControllerModule;
import io.swagger.config.ScannerFactory;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.config.ReflectiveJaxrsScanner;
import io.swagger.v3.jaxrs2.integration.JaxrsOpenApiContextBuilder;
import io.swagger.v3.oas.integration.OpenApiConfigurationException;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import org.jboss.resteasy.plugins.guice.GuiceResteasyBootstrapServletContextListener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by pwilson on 11/4/17.
 */
public class ApplicationContextListener extends GuiceResteasyBootstrapServletContextListener {

    @Override
    protected List<Module> getModules(ServletContext context) {
        context.setAttribute("resteasy.scan", "true");
        return ImmutableList.of(
                new WebModule(),
                new ControllerModule(),
                new RepositoryModule(),
                new AccessorModule(),
                new ConfigModule()
        );

    }

    @Override
    public void contextInitialized(ServletContextEvent event) {
        super.contextInitialized(event);
        configureOpenAPI();

    }

    private void configureOpenAPI() {
        OpenAPI openAPI = new OpenAPI();
        Info openApiInfo = new Info();
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
                                //scan our controller package.
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
    private SecurityScheme getEmergencyBasicAuthSecurity() {
        return new SecurityScheme()
                .name("basicAuth")
                .type(SecurityScheme.Type.HTTP)
                .scheme("basic");
    }

    private SecurityScheme getRabbitOAuthSecurity() {
        return new SecurityScheme()
                .name("rabbitOAuth")
                .type(SecurityScheme.Type.OAUTH2)
                .flows(new OAuthFlows()
                        .authorizationCode(new OAuthFlow()
                                .authorizationUrl(getRabbitAuthorizationUrl())
                                .tokenUrl(getRabbitTokenUrl())));
    }

    private String getRabbitAuthorizationUrl() {
        return Optional.ofNullable(System.getenv("CONFIG_RABBIT_AUTH_URL"))
                .orElse("http://rabbit-dev.inquestdevops.com/auth/login");
    }

    private String getRabbitTokenUrl() {
        return Optional.ofNullable(System.getenv("CONFIG_RABBIT_TOKEN_URL"))
                .orElse("http://rabbit-dev.inquestdevops.com/token");
    }

}