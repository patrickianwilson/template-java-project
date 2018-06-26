package com.patrickwilsonconsulting;

import com.google.common.collect.ImmutableList;
import com.google.inject.Module;
import com.patrickwilsonconsulting.accessors.AccessorModule;
import com.patrickwilsonconsulting.resource.StatusResource;
import com.patrickwilsonconsulting.resource.WebModule;
import com.patrickwilsonconsulting.repositories.RepositoryModule;
import com.patrickwilsonconsulting.controllers.ControllerModule;
import io.swagger.config.ScannerFactory;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.config.ReflectiveJaxrsScanner;
import org.jboss.resteasy.plugins.guice.GuiceResteasyBootstrapServletContextListener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import java.util.List;

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
                new AccessorModule()
        );

    }

    @Override
    public void contextInitialized(ServletContextEvent event) {
        super.contextInitialized(event);
        configureSwagger();

    }

    private BeanConfig configureSwagger() {
        ReflectiveJaxrsScanner scanner = new ReflectiveJaxrsScanner();
        scanner.setResourcePackage(StatusResource.class.getPackage().getName());
        ScannerFactory.setScanner(scanner);

        BeanConfig config = new BeanConfig();
        config.setBasePath("/");

        config.setTitle("Module Service API");
        config.setResourcePackage(StatusResource.class.getPackage().getName());
        config.setScan(true);

        return config;
    }
}