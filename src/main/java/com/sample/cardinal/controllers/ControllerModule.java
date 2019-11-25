package com.sample.cardinal.controllers;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Singleton;

/**
 * Created by pwilson on 11/4/17.
 */
public class ControllerModule implements Module {

    @Override
    public void configure(Binder binder) {
        binder.bind(UserController.class).to(ProductionUserController.class).in(Singleton.class);
    }

}
