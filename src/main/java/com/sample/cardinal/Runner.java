package com.sample.cardinal;

import com.google.inject.servlet.GuiceFilter;
import com.sample.cardinal.filters.CorsFilter;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

import javax.servlet.DispatcherType;
import java.net.BindException;
import java.util.EnumSet;

/**
 * Created by pwilson on 11/2/17.
 */
public class Runner {
    public static void main(String... args ) throws Exception {
//        System.out.println("Loading cert from config location: " + args[0]);
//        Config config = new ConfigBuilder()
//                .build();
//
//        KubernetesClient client = new DefaultKubernetesClient(config);
//
////        String ns = client.getNamespace();
//        NamespaceList namespaces = client.namespaces().list();
//        namespaces.getItems().stream().forEach(namespace -> System.out.println(namespace.getMetadata().getTemplateId()));
//
        Server server = new Server(8080);

        ServletContextHandler root = new ServletContextHandler(server, "/", ServletContextHandler.SESSIONS);

        root.addEventListener(new ApplicationContextListener());

        root.addFilter(CorsFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
        root.addFilter(GuiceFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));

        try {
            server.start();
        } catch(BindException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
