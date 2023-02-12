package com.sample.cardinal.resource;

import com.google.inject.Singleton;
import com.google.inject.servlet.ServletModule;
import com.inquestdevops.rabbit.agent.filter.RabbitFilter;
import com.inquestdevops.rabbit.agent.filter.handlers.BasicInvalidAuthenticationHandler;
import com.inquestdevops.rabbit.agent.filter.handlers.BasicUnauthorizedExceptionHandler;
import com.sample.cardinal.filters.CorsFilter;
import com.sample.cardinal.resource.exceptions.EntityAlreadyExistsExceptionHandler;
import com.sample.cardinal.resource.exceptions.EntityNotFoundExceptionHandler;
import com.sample.cardinal.resource.exceptions.InvalidInputExceptionHandler;
import com.sample.cardinal.resource.exceptions.UnknownServiceExceptionHandler;
import com.sample.cardinal.serializers.JsonStreamReaderWriter;
import io.swagger.v3.jaxrs2.SwaggerSerializers;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import org.jboss.resteasy.plugins.server.servlet.FilterDispatcher;

/**
 * Created by pwilson on 11/4/17.
 */
public class WebModule extends ServletModule {
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
        bind(RabbitFilter.class).in(Singleton.class);
        bind(BasicInvalidAuthenticationHandler.class).in(Singleton.class);
        bind(BasicUnauthorizedExceptionHandler.class).in(Singleton.class);

        //boot up the resteasy dispatcher.
        bind(FilterDispatcher.class).asEagerSingleton();
        bind(CorsFilter.class).asEagerSingleton();
        filter("/*").through(FilterDispatcher.class);
        filter("/*").through(CorsFilter.class);
    }
}
