package com.csye6225.webservice.RESTfulWebService.Service;

import com.csye6225.webservice.RESTfulWebService.Entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {

    String login(String username, String password) throws HttpClientErrorException.BadRequest;

    Optional<org.springframework.security.core.userdetails.User> findByToken(String token);

    List<User> findAll();

    User findByUsername(String username);

    User save(User user);

    UserDetails loadUserByUsername(String username);
}
