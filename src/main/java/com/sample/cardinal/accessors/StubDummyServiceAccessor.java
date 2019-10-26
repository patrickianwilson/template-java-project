package com.sample.cardinal.accessors;

/**
 * Image this package contains the logic to call remote services and adapt their interfaces for internal use.
 */
public class StubDummyServiceAccessor implements DummyServiceAccessor {

    @Override
    public String getMessage() {
        return "Hello World";
    }
}
