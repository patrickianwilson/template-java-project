package com.sample.cardinal.locator;

public class WebAppLocator {

    public static String locateWebAppURL() {
        String buildConfName = System.getenv("TEAMCITY_BUILDCONF_NAME");  //fetch build conf name from TeamCIty build env
        if (buildConfName == null) {
            //localhost testing
            return "http://localhost:8888/";
        } else if (buildConfName.toLowerCase().contains("dev")) {
            return "http://cardinal-sample-dev.inquestdevops.com/";
        } else if (buildConfName.toLowerCase().contains("prod")) {
            return "http://cardinal-sample-prod.inquestdevops.com/";
        } else {
            throw new RuntimeException("Unconfigured Build Configuration name: " + buildConfName);
        }
    }

    public static boolean shouldRunHeadless() {
        String buildConfName = System.getenv("TEAMCITY_BUILDCONF_NAME");
        if (buildConfName == null) {
            //localhost testing
            return false;
        } else {
            return true; //locally run browser mode for more debugability
        }
    }
}
