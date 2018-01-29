package com.patrickwilsonconsulting.service;

import com.google.inject.Binder;
import com.google.inject.Module;

/**
 * Created by pwilson on 11/4/17.
 */
public class ServiceModule implements Module {

    @Override
    public void configure(Binder binder) {
        binder.bind(UserService.class).to(ProductionUserService.class);
    }

}
