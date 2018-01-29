package com.patrickwilsonconsulting.service.integration;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.patrickwilsonconsulting.controllers.UserController;
/**
 * Created by pwilson on 11/27/17.
 */
public class TestServiceModule implements Module {

    @Override
    public void configure(Binder binder) {
        //bind our controller here.  We don't need all the web integration layer.
        binder.bind(UserController.class);
    }

}
