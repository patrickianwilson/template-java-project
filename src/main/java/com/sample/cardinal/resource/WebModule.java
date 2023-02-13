package com.sample.cardinal.resource;

import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.servlet.ServletModule;
import com.inquestdevops.rabbit.agent.AuthorizationManager;
import com.inquestdevops.rabbit.agent.DefaultAuthorizationManager;
import com.inquestdevops.rabbit.agent.filter.PermissionsProvider;
import com.inquestdevops.rabbit.agent.filter.RabbitFilter;
import com.inquestdevops.rabbit.agent.filter.RabbitPermissionsProvider;
import com.inquestdevops.rabbit.agent.filter.handlers.BasicInvalidAuthenticationHandler;
import com.inquestdevops.rabbit.agent.filter.handlers.BasicUnauthorizedExceptionHandler;
import com.inquestdevops.rabbit.client.ApiClient;
import com.inquestdevops.rabbit.client.api.DefaultApi;
import com.sample.cardinal.config.ApplicationConfig;
import com.sample.cardinal.filters.CorsFilter;
import com.sample.cardinal.resource.exceptions.EntityAlreadyExistsExceptionHandler;
import com.sample.cardinal.resource.exceptions.EntityNotFoundExceptionHandler;
import com.sample.cardinal.resource.exceptions.InvalidInputExceptionHandler;
import com.sample.cardinal.resource.exceptions.UnknownServiceExceptionHandler;
import com.sample.cardinal.resource.view.AccountLoginResource;
import com.sample.cardinal.serializers.JsonStreamReaderWriter;
import io.swagger.v3.jaxrs2.SwaggerSerializers;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import org.jboss.resteasy.plugins.server.servlet.FilterDispatcher;

import javax.inject.Named;
import java.util.concurrent.TimeUnit;

/**
 * Created by pwilson on 11/4/17.
 */
public class WebModule extends ServletModule {

    private final String componentName;
    public WebModule(String componentName) {
        this.componentName = componentName;
    }
    @Override
    public void configureServlets() {

        bind(StatusResource.class).asEagerSingleton();
        bind(DeepStatusResource.class).asEagerSingleton();
        bind(UserResource.class).in(Singleton.class);
        bind(JsonStreamReaderWriter.class).in(Singleton.class);

        //exception handlers
        bind(EntityAlreadyExistsExceptionHandler.class).in(Singleton.class);
        bind(EntityNotFoundExceptionHandler.class).in(Singleton.class);
        bind(InvalidInputExceptionHandler.class).in(Singleton.class);
        bind(UnknownServiceExceptionHandler.class).in(Singleton.class);

        //hook up swagger annotations
        bind(OpenApiResource.class).asEagerSingleton();
        bind(SwaggerSerializers.class).in(Singleton.class);

        //hookup authentication and InvalidAuthentication exception handler
        bind(BasicInvalidAuthenticationHandler.class).in(Singleton.class);
        bind(BasicUnauthorizedExceptionHandler.class).in(Singleton.class);

        //hookup views
        bind(AccountLoginResource.class).in(Singleton.class);

        //boot up the resteasy dispatcher.
        bind(FilterDispatcher.class).asEagerSingleton();
        bind(CorsFilter.class).asEagerSingleton();
        filter("/*").through(FilterDispatcher.class);
        filter("/*").through(CorsFilter.class);
    }

    @Provides
    public RabbitFilter defineRabbitFilter(Provider<DefaultApi> rabbitClientProvider, PermissionsProvider permissionsProvider) {
        return new RabbitFilter(rabbitClientProvider, permissionsProvider);
    }



    @Provides
    public DefaultApi getRabbitClient(ApplicationConfig applicationConfig) {

        DefaultApi rabbitClient = new DefaultApi(
                (new ApiClient())
                        .setBasePath(applicationConfig.rabbitBasePath())
                        .setReadTimeout((int) TimeUnit.SECONDS.toMillis(20L))
                        .setConnectTimeout((int)TimeUnit.SECONDS.toMillis(10L))
        );
        return rabbitClient;
    }

    @Provides
    public AuthorizationManager getAuthManager() {
        return new DefaultAuthorizationManager(this.componentName);
    }

    @Provides
    public PermissionsProvider getPermissionsProvider(Provider<DefaultApi> rabbitClientProvider) {
        return new RabbitPermissionsProvider(rabbitClientProvider, this.componentName + ".*");
    }





}
