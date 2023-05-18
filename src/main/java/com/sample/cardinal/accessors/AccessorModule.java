package com.sample.cardinal.accessors;

import com.google.inject.Binder;
import com.google.inject.Module;

public class AccessorModule implements Module {

    @Override
    public void configure(Binder binder) {
        binder.bind(DummyServiceAccessor.class).to(StubDummyServiceAccessor.class);
    }
}
