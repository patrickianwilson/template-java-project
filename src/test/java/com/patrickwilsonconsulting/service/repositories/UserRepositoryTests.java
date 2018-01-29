package com.patrickwilsonconsulting.service.repositories;

import com.google.cloud.datastore.DatastoreOptions;
import com.google.common.collect.ImmutableMap;
import com.patrickwilson.ardm.api.key.EntityKey;
import com.patrickwilson.ardm.api.repository.QueryResult;
import com.patrickwilson.ardm.datasource.api.DataSourceAdaptor;
import com.patrickwilson.ardm.datasource.gcp.datastore.GCPDatastoreDatasourceAdaptor;
import com.patrickwilson.ardm.proxy.RepositoryProvider;
import com.patrickwilsonconsulting.builders.UserBuilder;
import com.patrickwilsonconsulting.controllers.model.User;
import com.patrickwilsonconsulting.repositories.UserRepository;
import com.patrickwilsonconsulting.repositories.model.UserEntity;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;


/**
 * Created by pwilson on 11/24/17.
 */
public class UserRepositoryTests {

    private static UserRepository userRepo = null;
    private static UserBuilder userBuilder = null;
    private static User defaultAppEntity = null;
    private static EntityKey defaultEntityKey = null;
    private static List<UserEntity> createdUsers = new ArrayList<>();


    @BeforeClass
    public static void setupClass() {
        RepositoryProvider repos = new RepositoryProvider();

        DataSourceAdaptor gcp = new GCPDatastoreDatasourceAdaptor(DatastoreOptions.getDefaultInstance().getService());
        userRepo = repos.bind(UserRepository.class).to(gcp);

        userBuilder = new UserBuilder(userRepo);
        defaultAppEntity = defaultAppEntity();

        UserEntity created =  userRepo.save(userBuilder.toEntity(defaultAppEntity));
        defaultEntityKey = created.getId();
        createdUsers.add(created);

    }

    private static User defaultAppEntity() {
        User user = new User();
        user.setUserId("serlingarcher");
        user.setName("archer");

        user.setSampleMap(ImmutableMap.<String,String>builder()
                .put("biggest_fear", "crocodiles")
                .put("second_fear", "alligators")
                .put("third_fear", "aneurysm")
                .build());

        return user;
    }


    @AfterClass
    public static void teardownClass() {
        for (UserEntity app: createdUsers) {
            userRepo.delete(app);
         }
    }

    @Test
    public void createAndReadSuccess() {

        User result = userBuilder.toApiModel(userRepo.findOne(defaultEntityKey));
        Assert.assertNotNull(result);
        Assert.assertEquals("Complete User Entity Loop didn't produce the same api model for User.", defaultAppEntity, result);
    }

    @Test
    public void findAllUsersNamedArcher() {
        QueryResult<UserEntity> matching = userRepo.findByName("archer");

        Assert.assertNotNull(matching);
        Assert.assertThat(matching.getNumResults(), is(1));
        Assert.assertNotNull(matching.getResults());
        Assert.assertNotNull(matching.getResults().get(0));
        Assert.assertThat(matching.getResults().get(0).getName(), is("archer"));
    }




}
