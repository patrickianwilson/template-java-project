package com.sample.cardinal;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.google.inject.servlet.GuiceFilter;
import com.sample.cardinal.filters.CorsFilter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

import javax.servlet.DispatcherType;
import java.net.BindException;
import java.util.EnumSet;

/**
 * Created by pwilson on 11/2/17.
 */
public class Runner {
    //fixme - update with the desired component name.
    private static String componentName = "ChangeMe!";
    public static void main(String... rawArgs ) throws Exception {

        Args args = new Args();
        JCommander.newBuilder()
                .addObject(args)
                .build()
                .parse(rawArgs);

        Server server = new Server(args.port);

        ServletContextHandler root = new ServletContextHandler(server, "/", ServletContextHandler.SESSIONS);

        root.addEventListener(new ApplicationContextListener(componentName));

        root.addFilter(CorsFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
        root.addFilter(GuiceFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));

        try {
            server.start();
        } catch(BindException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Args {

        @Parameter(names={"-p", "--port"}, description = "The port to start the service on.")
        int port = 8080;
    }
}
