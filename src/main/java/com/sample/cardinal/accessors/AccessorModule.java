package com.sample.cardinal.accessors;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Singleton;

public class AccessorModule implements Module {

    @Override
    public void configure(Binder binder) {
        //assuming these accessors are threadsafe...
        binder.bind(DummyServiceAccessor.class).to(StubDummyServiceAccessor.class).in(Singleton.class);
    }
}
