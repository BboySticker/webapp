package com.csye6225.webservice.RESTfulWebService.Dao;

import com.csye6225.webservice.RESTfulWebService.Entity.User.User;

import java.util.List;

public interface UserDao {

    List<User> findAll();

    User findByUsername(String username);
    
    void save(User user);

}
