package com.sample.cardinal.config;

import com.google.common.collect.Lists;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.inquestdevops.config.EnvironmentAwareOverridingClasspathConfigSource;
import org.cfg4j.provider.ConfigurationProvider;
import org.cfg4j.provider.ConfigurationProviderBuilder;
import org.cfg4j.source.ConfigurationSource;
import org.cfg4j.source.context.environment.ImmutableEnvironment;

import javax.inject.Named;
import java.nio.file.Paths;
import java.util.Optional;

public class ConfigModule implements Module {

    @Override
    public void configure(Binder binder) {

    }

    @Named("CassiusApplicationName")
    @Provides
    public String getCassiusApplicationName() {
        return Optional.ofNullable(System.getenv("CASSIUS_APP_NAME"))
                .orElse("LocalApp");
    }

    @Named("CassiusEnvironmentName")
    @Provides
    public String getCassiusEnvironmentName() {
        return Optional.ofNullable(System.getenv("CASSIUS_ENV_NAME"))
                .orElse("LocalEnv");
    }

    @Provides
    public ConfigurationProvider getConfigProvider(@Named("CassiusEnvironmentName") String envName) {
        ConfigurationSource source = new EnvironmentAwareOverridingClasspathConfigSource(() ->
                Lists.newArrayList(Paths.get("app-config.yaml")), ConfigModule.class);

        return new ConfigurationProviderBuilder()
                .withEnvironment(new ImmutableEnvironment(envName))
                .withConfigurationSource(source)
                .build();
    }

    @Provides
    public ApplicationConfig getApplicationConfig(ConfigurationProvider provider) {
        return provider.bind("application", ApplicationConfig.class);
    }
}
