package com.sample.cardinal.repositories;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.patrickwilson.ardm.datasource.api.DataSourceAdaptor;
import com.patrickwilson.ardm.datasource.gcp.datastore.GCPDatastoreDatasourceAdaptor;
import com.patrickwilson.ardm.proxy.RepositoryProvider;
import com.sample.cardinal.builders.UserBuilder;

import javax.inject.Named;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;

/**
 * Created by pwilson on 11/4/17.
 */
public class RepositoryModule implements Module {
    public static final String CREDENTIALS_KEY = "GOOGLE_APPLICATION_CREDENTIALS";

    @Override
    public void configure(Binder binder) {
        binder.bind(UserBuilder.class);
    }

    @Provides
    public UserRepository provideUserRepositoryRepositoryImpl(RepositoryProvider repos, DataSourceAdaptor adaptor) {
        return repos.bind(UserRepository.class).to(adaptor);
    }


    @Provides
    public DataSourceAdaptor provideRepoAdaptor(@Named("CassiusApplicationName") String appName,
                                                @Named("CassiusEnvironmentName") String envName,
                                                @Named("GoogleCreds") Credentials googleCreds) {
        String ns = String.format("%s_%s", appName, envName);
        return new GCPDatastoreDatasourceAdaptor(DatastoreOptions
                .newBuilder()
                .setNamespace(ns)
                .setCredentials(googleCreds)
                .build()
                .getService());
    }

    @Provides
    public RepositoryProvider provideRepositoryProvider() {
        return new RepositoryProvider();
    }

    @Provides
    @Named("GoogleCreds")
    public Credentials getGoogleCredentials() {
        try {
            return GoogleCredentials
                    .fromStream(
                            new FileInputStream(
                                    Optional.of(System.getenv(CREDENTIALS_KEY))
                                            .orElseThrow(() ->
                                                    new RuntimeException("Unable To Locate Google Credentials Environment Variable!"))))
                    .createScoped("https://www.googleapis.com/auth/cloud-platform");
        } catch (IOException e) {
            throw new RuntimeException("Unable to open file at " + System.getenv(CREDENTIALS_KEY), e);
        }

    }
}
