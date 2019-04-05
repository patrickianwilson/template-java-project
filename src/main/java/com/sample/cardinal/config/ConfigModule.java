package com.sample.cardinal.config;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;

import javax.inject.Named;
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
}
