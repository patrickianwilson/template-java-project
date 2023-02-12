package com.sample.cardinal;

import com.google.common.collect.ImmutableList;
import com.google.inject.Module;
import com.sample.cardinal.accessors.AccessorModule;
import com.sample.cardinal.config.ConfigModule;
import com.sample.cardinal.controllers.ControllerModule;
import com.sample.cardinal.repositories.RepositoryModule;
import com.sample.cardinal.resource.WebModule;
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
                new AccessorModule(),
                new ConfigModule()
        );
    }

    @Override
    public void contextInitialized(ServletContextEvent event) {
        super.contextInitialized(event);
    }

}