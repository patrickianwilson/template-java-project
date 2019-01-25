package com.sample.cardinal.repositories;

import com.google.cloud.datastore.DatastoreOptions;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.patrickwilson.ardm.datasource.api.DataSourceAdaptor;
import com.patrickwilson.ardm.datasource.gcp.datastore.GCPDatastoreDatasourceAdaptor;
import com.patrickwilson.ardm.proxy.RepositoryProvider;
import com.sample.cardinal.builders.UserBuilder;

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
    public DataSourceAdaptor provideRepoAdaptor() {
        return new GCPDatastoreDatasourceAdaptor(DatastoreOptions.getDefaultInstance().getService());
    }

    @Provides
    public RepositoryProvider provideRepositoryProvider() {
        return new RepositoryProvider();
    }
}
