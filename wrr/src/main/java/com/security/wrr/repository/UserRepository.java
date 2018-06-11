package com.security.wrr.repository;

import com.security.wrr.model.MyDownload;
import com.security.wrr.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends MongoRepository<User,String> {

    List<User> findByUsernameAndPassword(@Param("userName") String userName, @Param("password") String password);
}
