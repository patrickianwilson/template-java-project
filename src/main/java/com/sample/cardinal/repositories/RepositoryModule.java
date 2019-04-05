package com.sample.cardinal.repositories;

import com.google.cloud.datastore.DatastoreOptions;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.patrickwilson.ardm.datasource.api.DataSourceAdaptor;
import com.patrickwilson.ardm.datasource.gcp.datastore.GCPDatastoreDatasourceAdaptor;
import com.patrickwilson.ardm.proxy.RepositoryProvider;
import com.sample.cardinal.builders.UserBuilder;

import javax.inject.Named;

/**
 * Created by pwilson on 11/4/17.
 */
public class RepositoryModule implements Module {
    @Override
    public void configure(Binder binder) {
        binder.bind(UserBuilder.class);
    }

    @Provides
    public UserRepository provideUserRepositoryRepositoryImpl(RepositoryProvider repos, DataSourceAdaptor adaptor) {
        return repos.bind(UserRepository.class).to(adaptor);
    }


    @Provides
    public DataSourceAdaptor provideRepoAdaptor(@Named("CassiusApplicationName") String appName, @Named("CassiusEnvironmentName") String envName) {
        String ns = String.format("%s_%s", appName, envName);
        return new GCPDatastoreDatasourceAdaptor(DatastoreOptions
                .newBuilder()
                .setNamespace(ns)
                .build()
                .getService());
    }

    @Provides
    public RepositoryProvider provideRepositoryProvider() {
        return new RepositoryProvider();
    }
}
