package com.patrickwilsonconsulting.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by pwilson on 12/5/17.
 */
public class CommonValidators {
    private static Pattern RESOURCE_NAME_REGEX = Pattern.compile("[a-z.-]{5,253}");

    /**
     * Test whether a string matches the kubernetes resource name requirements.
     * @param name
     * @return
     */
    public static boolean isValidResourceName(String name) {
        Matcher m = RESOURCE_NAME_REGEX.matcher(name);
        return m.matches();
    }
}
