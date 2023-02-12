package com.sample.cardinal.config;

public interface ApplicationConfig {
    String oauthAuthorizationUrl();
    String oauthCompletionUrl();
    String oauthTokenUrl();
    String rabbitBasePath();
    Integer port();
}
