package com.sample.cardinal.resource;

import com.google.inject.servlet.ServletModule;
import com.sample.cardinal.resource.exceptions.EntityAlreadyExistsExceptionHandler;
import com.sample.cardinal.resource.exceptions.EntityNotFoundExceptionHandler;
import com.sample.cardinal.resource.exceptions.InvalidInputExceptionHandler;
import com.sample.cardinal.filters.CorsFilter;
import com.sample.cardinal.serializers.JsonStreamReaderWriter;
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

        bind(StatusResource.class);
        bind(UserResource.class);
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
