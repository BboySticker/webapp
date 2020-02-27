package com.csye6225.webservice.RESTfulWebService.Service;

import com.csye6225.webservice.RESTfulWebService.Entity.User.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    List<User> findAll();

    User findByUsername(String username);

    User save(User user);

    UserDetails loadUserByUsername(String username);
}
