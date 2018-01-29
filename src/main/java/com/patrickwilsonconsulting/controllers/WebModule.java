package com.patrickwilsonconsulting.controllers;

import com.google.inject.servlet.ServletModule;
import com.patrickwilsonconsulting.controllers.exceptions.EntityAlreadyExistsExceptionHandler;
import com.patrickwilsonconsulting.controllers.exceptions.EntityNotFoundExceptionHandler;
import com.patrickwilsonconsulting.controllers.exceptions.InvalidInputExceptionHandler;
import com.patrickwilsonconsulting.filters.CorsFilter;
import com.patrickwilsonconsulting.serializers.JsonStreamReaderWriter;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import org.jboss.resteasy.plugins.server.servlet.FilterDispatcher;

/**
 * Created by pwilson on 11/4/17.
 */
public class WebModule extends ServletModule {
    @Override
    public void configureServlets() {

        bind(StatusController.class);
        bind(UserController.class);
        bind(JsonStreamReaderWriter.class);
        configureSwagger();

        //exception handlers
        bind(EntityAlreadyExistsExceptionHandler.class);
        bind(EntityNotFoundExceptionHandler.class);
        bind(InvalidInputExceptionHandler.class);

        //hook up swagger annotations
        bind(ApiListingResource.class);
        bind(SwaggerSerializers.class);
        //boot up the resteasy dispatcher.
        bind(FilterDispatcher.class).asEagerSingleton();
        bind(CorsFilter.class).asEagerSingleton();
        filter("/*").through(FilterDispatcher.class);
        filter("/*").through(CorsFilter.class);
    }

    private BeanConfig configureSwagger() {
        BeanConfig config = new BeanConfig();

        config.setBasePath("localhost:8080");
        config.setTitle("Template Cardinal Service API");
        config.setScan(true);

        return config;
    }
}
