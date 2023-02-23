package com.sample.cardinal.controllers.integration;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.sample.cardinal.resource.UserResource;
/**
 * Created by pwilson on 11/27/17.
 */
public class TestServiceModule implements Module {

    @Override
    public void configure(Binder binder) {
        //bind our controller here.  We don't need all the web integration layer.
        binder.bind(UserResource.class);
    }

}