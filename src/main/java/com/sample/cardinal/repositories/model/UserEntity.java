package com.sample.cardinal.repositories.model;

import com.patrickwilson.ardm.api.annotation.Entity;
import com.patrickwilson.ardm.api.annotation.Indexed;
import com.patrickwilson.ardm.api.key.EntityKey;
import com.patrickwilson.ardm.api.key.Key;
import lombok.*;

import java.util.List;

/**
 * Created by pwilson on 11/4/17.
 */
@Entity(domainOrTable = "User")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    private EntityKey id;
    private String email;
    private String name;

    private List<KeyValuePair> sampleMap;

    @Key(com.google.cloud.datastore.Key.class)
    public void setId(EntityKey id) {
        this.id = id;
    }

    @Indexed
    public void setName(String name) {
        this.name = name;
    }
}
