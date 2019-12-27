package com.sample.cardinal.serializers;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.inquestdevops.cardinal.base.model.ApiObject;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;

/**
 * Created by pwilson on 4/4/16.
 */
@Provider
@Singleton
@Produces({ MediaType.APPLICATION_JSON, "text/json" })
@Consumes({ MediaType.APPLICATION_JSON, "text/json" })
public class JsonStreamReaderWriter implements MessageBodyWriter<ApiObject>, MessageBodyReader<ApiObject> {

    /**
     * Classes listed by name to avoid jar dependencies.
     */
    private static final Set<String> INTERNAL_CLASSES = ImmutableSet.of(
            "com.sun.jersey.api.view.Viewable",
            "javax.ws.rs.core.StreamingOutput",
            "javax.ws.rs.core.Response"
    );

    private final Set<String> doNotHandleList = Sets.newHashSet(INTERNAL_CLASSES);;

    private final Gson gson;

    @Inject
    public JsonStreamReaderWriter(Gson gson) {
        this.gson = gson;
    }


    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        if (!supported(type))
            return false;

        return type != String.class;
    }

    @Override
    public ApiObject readFrom(Class<ApiObject> type, Type genericType, Annotation[] annotations, MediaType mediaType,
                           MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException {
        try (InputStreamReader streamReader = new InputStreamReader(entityStream, Charsets.UTF_8)) {
            return gson.fromJson(streamReader, genericType);
        }
    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        if (!supported(type))
            return false;

        // Can write anything
        return true;
    }

    @Override
    public long getSize(ApiObject object, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        // -1 if the length cannot be determined in advance
        return -1;
    }

    @Override
    public void writeTo(ApiObject object, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
                        MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException {
        try (OutputStreamWriter writer = new OutputStreamWriter(entityStream, Charsets.UTF_8)) {
            if (genericType != null)
                gson.toJson(object, genericType, writer);
            else
                gson.toJson(object, writer);
        }
    }

    /**
     * Check for some unsupported classes.
     */
    private boolean supported(Class<?> type) {
        if (doNotHandleList.contains(type.getName()))
            return false;

        return true;
    }
}