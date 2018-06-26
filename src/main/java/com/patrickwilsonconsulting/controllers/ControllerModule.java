package com.patrickwilsonconsulting.controllers;

import com.google.inject.Binder;
import com.google.inject.Module;

/**
 * Created by pwilson on 11/4/17.
 */
public class ControllerModule implements Module {

    @Override
    public void configure(Binder binder) {
        binder.bind(UserController.class).to(ProductionUserController.class);
    }

}
