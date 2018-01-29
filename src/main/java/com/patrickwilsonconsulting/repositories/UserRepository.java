package com.patrickwilsonconsulting.repositories;

import com.patrickwilson.ardm.api.annotation.Query;
import com.patrickwilson.ardm.api.annotation.Repository;
import com.patrickwilson.ardm.api.repository.CRUDRepository;
import com.patrickwilson.ardm.api.repository.QueryResult;
import com.patrickwilsonconsulting.repositories.model.UserEntity;

/**
 * Created by pwilson on 11/4/17.
 */
@Repository(UserEntity.class)
public interface UserRepository extends CRUDRepository<UserEntity> {
    @Query
    QueryResult<UserEntity> findByName(String name);
}
