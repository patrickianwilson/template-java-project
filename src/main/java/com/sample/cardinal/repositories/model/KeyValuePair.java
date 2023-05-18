package com.sample.cardinal.repositories.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by pwilson on 11/24/17.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeyValuePair {
    private String key;
    private String value;
}
