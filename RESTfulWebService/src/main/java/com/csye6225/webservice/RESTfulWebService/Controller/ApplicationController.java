package com.csye6225.webservice.RESTfulWebService.Controller;

import com.csye6225.webservice.RESTfulWebService.Entity.User;
import com.csye6225.webservice.RESTfulWebService.Exception.*;
import com.csye6225.webservice.RESTfulWebService.Service.UserService;
import com.csye6225.webservice.RESTfulWebService.Validation.EmailValidator;
import com.csye6225.webservice.RESTfulWebService.Validation.PasswordValidator;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@RestController
public class ApplicationController {

    @Autowired
    private UserService userService;

    private EmailValidator emailValidator = new EmailValidator();

    private PasswordValidator passwordValidator = new PasswordValidator();

    private Logger logger = Logger.getLogger(getClass().getName());

    @GetMapping("/users")
    public MappingJacksonValue findAll() {

        List<User> users = userService.findAll();

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.
                filterOutAllExcept("id", "username", "first_name", "last_name", "email", "account_created", "account_updated");

        FilterProvider filters = new SimpleFilterProvider().addFilter("UserFilter", filter);

        MappingJacksonValue mapping = new MappingJacksonValue(users);

        mapping.setFilters(filters);

        return mapping;
    }

    @PostMapping("/v1/user")
    public MappingJacksonValue createUser(@RequestBody User user) {

        String username = user.getEmail_address();

        if (!emailValidator.isValid(username, null)) {
            logger.warning("Invalid email!");
            throw new EmailInvalidException("Invalid email!");
        }

        String password = user.getPassword();
        if (password.length() < 8 || !passwordValidator.isValid(password)) {
            throw new WeakPasswordException("Password Too Weak!");
        }

        logger.info("Processing registration form for: " + username);

        // check the database if user already exists
        User existing = userService.findByUsername(username);
        if (existing != null){
            logger.warning("Username already exist!");
            throw new UserAlreadyExist("Username already exist!");
        }

        // create user account
        user.setId(0);
        user.setAccount_created(new Date());
        User savedUser = userService.save(user);

        logger.info("Successfully created user: " + username);

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.
                filterOutAllExcept("id", "first_name", "last_name", "email_address", "account_created", "account_updated");

        FilterProvider filters = new SimpleFilterProvider().addFilter("UserFilter", filter);

        MappingJacksonValue mapping = new MappingJacksonValue(savedUser);

        mapping.setFilters(filters);

        return mapping;
    }

    @GetMapping("/v1/user/self")
    public MappingJacksonValue findOne() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String username = auth.getName();

        logger.info("Successfully obtained user: " + username);

        User user = userService.findByUsername(username);

        if (user == null) {
            throw new UserNotFoundException("User Not Found!");
        }

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.
                filterOutAllExcept("id", "first_name", "last_name", "email_address", "account_created", "account_updated");

        FilterProvider filters = new SimpleFilterProvider().addFilter("UserFilter", filter);

        MappingJacksonValue mapping = new MappingJacksonValue(user);

        mapping.setFilters(filters);

        return mapping;
    }

    @PutMapping("/v1/user/self")
    public MappingJacksonValue update(@RequestBody User user) {

        if (!user.getEmail_address().isEmpty()
                || user.getAccount_created() != null
                || user.getAccount_updated() != null) {
            throw new FieldRestrictedException("Fields not allowed to modify; " +
                    "Only can change FirstName, LastName and Password.");
        }

        String password = user.getPassword();
        System.out.println(password);
        if (password != null && (password.length() < 8 || !passwordValidator.isValid(password))) {
            throw new WeakPasswordException("Password Too Weak!");
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String username = auth.getName();

        User old = userService.findByUsername(username);

        // cannot change attributes except first_name, last_name and password
        user.setId(old.getId());
        user.setEmail_address(old.getEmail_address());
        user.setAccount_created(old.getAccount_created());
        user.setToken(old.getToken());

        // save the composed user
        User savedUser = userService.save(user);

        // set basic filter, avoid returning sensitive info such as password
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.
                filterOutAllExcept("id", "first_name", "last_name", "email_address", "account_created", "account_updated");

        FilterProvider filters = new SimpleFilterProvider().addFilter("UserFilter", filter);

        MappingJacksonValue mapping = new MappingJacksonValue(savedUser);

        mapping.setFilters(filters);

        return mapping;
    }

}
