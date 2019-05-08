package com.sample.java;

import com.google.common.collect.Lists;
import com.inquestdevops.config.EnvironmentAwareOverridingClasspathConfigSource;
import org.cfg4j.provider.ConfigurationProvider;
import org.cfg4j.provider.ConfigurationProviderBuilder;
import org.cfg4j.source.ConfigurationSource;
import org.cfg4j.source.context.environment.ImmutableEnvironment;

import java.nio.file.Paths;

/**
 * Created by pwilson on 11/2/17.
 */
public class Runner {

    public static void main(String... args ) {

        ConfigurationSource source = new EnvironmentAwareOverridingClasspathConfigSource(() ->
                Lists.newArrayList(Paths.get("app-config.yaml")));

        ConfigurationProvider provider =  new ConfigurationProviderBuilder()
                .withConfigurationSource(source)
                .build();

        SampleConfig appConfig = provider.bind("application", SampleConfig.class);

        System.out.println(appConfig.message());
        System.out.println("Loading Version: " + appConfig.version());
    }
}
