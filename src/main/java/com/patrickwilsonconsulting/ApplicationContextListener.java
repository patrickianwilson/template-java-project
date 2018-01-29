package com.patrickwilsonconsulting;

import com.google.common.collect.ImmutableList;
import com.google.inject.Module;
import com.patrickwilsonconsulting.controllers.StatusController;
import com.patrickwilsonconsulting.controllers.WebModule;
import com.patrickwilsonconsulting.repositories.RepositoryModule;
import com.patrickwilsonconsulting.service.ServiceModule;
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
                new ServiceModule(),
                new RepositoryModule()
        );

    }

    @Override
    public void contextInitialized(ServletContextEvent event) {
        super.contextInitialized(event);
        configureSwagger();

    }

    private BeanConfig configureSwagger() {
        ReflectiveJaxrsScanner scanner = new ReflectiveJaxrsScanner();
        scanner.setResourcePackage(StatusController.class.getPackage().getName());
        ScannerFactory.setScanner(scanner);

        BeanConfig config = new BeanConfig();
        config.setBasePath("/");

        config.setTitle("Module Service API");
        config.setResourcePackage(StatusController.class.getPackage().getName());
        config.setScan(true);

        return config;
    }
}