package com.csye6225.webservice.RESTfulWebService.Dao;

import com.csye6225.webservice.RESTfulWebService.Entity.User;

import javax.naming.AuthenticationException;
import java.util.List;
import java.util.Optional;

public interface UserDao {

    List<User> findAll();

    User findByUsername(String username);
    
    User save(User user);

    User login(String username, String password);

    Optional<User> findByToken(String token);
}
