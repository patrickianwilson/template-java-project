package com.sample.cardinal.controllers.integration;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.sample.cardinal.builders.UserBuilder;
import com.sample.cardinal.config.ConfigModule;
import com.sample.cardinal.resource.UserResource;
import com.sample.cardinal.resource.model.User;
import com.sample.cardinal.repositories.RepositoryModule;
import com.sample.cardinal.repositories.UserRepository;
import com.sample.cardinal.controllers.ControllerModule;
import lombok.extern.slf4j.Slf4j;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;

/**
 * Created by pwilson on 11/27/17.
 */
@Slf4j
public class UserResourceIntegrationTest {

    public static final String USER_NAME = "archer";
    static UserResource userResource = null;
    static UserRepository appRepo;
    static UserBuilder appBuilder;
    static List<User> createdUsers = new ArrayList<>();

    @BeforeClass
    public static void setup() {
        Injector context = Guice.createInjector(new TestServiceModule(),
                new RepositoryModule(),
                new ControllerModule(),
                new ConfigModule());
        userResource = context.getInstance(UserResource.class);
        appRepo = context.getInstance(UserRepository.class);
        appBuilder = context.getInstance(UserBuilder.class);
    }

    @AfterClass
    public static void cleanup() {
        try {
            createdUsers.stream()
                .map(application -> appBuilder.toEntity(application))
                .forEach(applicationEntity -> {
                    appRepo.delete(applicationEntity);
                });
        } catch (Exception e) {
            log.warn("unabled to cleanup user database entities", e);
        }

    }

    @Test
    public void testCreateUserSuccess() throws URISyntaxException {

        User app = new User();
        app.setName("Sterling Archer");
        app.setUserId("sterlingarcher");

        createdUsers.add(app);
        Response createAppResp = userResource.createUser(app);
        Assert.assertThat(createAppResp.getStatus(), is(201));

    }


}
