package com.sample.cardinal.resource;

import com.google.inject.servlet.ServletModule;
import com.inquestdevops.rabbit.agent.filter.RabbitFilter;
import com.inquestdevops.rabbit.agent.filter.handlers.BasicInvalidAuthenticationHandler;
import com.sample.cardinal.filters.CorsFilter;
import com.sample.cardinal.resource.exceptions.EntityAlreadyExistsExceptionHandler;
import com.sample.cardinal.resource.exceptions.EntityNotFoundExceptionHandler;
import com.sample.cardinal.resource.exceptions.InvalidInputExceptionHandler;
import com.sample.cardinal.serializers.JsonStreamReaderWriter;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import org.jboss.resteasy.plugins.server.servlet.FilterDispatcher;

/**
 * Created by pwilson on 11/4/17.
 */
public class WebModule extends ServletModule {
    @Override
    public void configureServlets() {

        bind(StatusResource.class);
        bind(DeepStatusResource.class);
        bind(UserResource.class);
        bind(JsonStreamReaderWriter.class);

        //exception handlers
        bind(EntityAlreadyExistsExceptionHandler.class);
        bind(EntityNotFoundExceptionHandler.class);
        bind(InvalidInputExceptionHandler.class);

        //hook up swagger annotations
        bind(OpenApiResource.class);
        bind(SwaggerSerializers.class);

        //hookup authentication and InvalidAuthentication exception handler
        bind(RabbitFilter.class);
        bind(BasicInvalidAuthenticationHandler.class);

        //boot up the resteasy dispatcher.
        bind(FilterDispatcher.class).asEagerSingleton();
        bind(CorsFilter.class).asEagerSingleton();
        filter("/*").through(FilterDispatcher.class);
        filter("/*").through(CorsFilter.class);
    }
}
