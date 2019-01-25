package com.sample.cardinal.resource.model;

import com.sample.cardinal.serializers.ApiObject;
import lombok.Data;

import java.util.Map;

/**
 * Created by pwilson on 11/4/17.
 */
@Data
public class User implements ApiObject {
    private String userId;
    private String name;
    private Map<String, String> sampleMap;
}
