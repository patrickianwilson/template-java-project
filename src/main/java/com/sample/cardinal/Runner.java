package com.sample.cardinal;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.jetty.server.Server;

import java.net.BindException;

/**
 * Created by pwilson on 11/2/17.
 */
public class Runner {
    public static void main(String... rawArgs ) throws Exception {

        Args args = new Args();
        JCommander.newBuilder()
                .addObject(args)
                .build()
                .parse(rawArgs);

        Server server = new Server(args.port);

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
