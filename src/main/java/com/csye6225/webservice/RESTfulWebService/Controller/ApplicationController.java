package com.csye6225.webservice.RESTfulWebService.Controller;

import com.csye6225.webservice.RESTfulWebService.Entity.User.User;
import com.csye6225.webservice.RESTfulWebService.Exception.*;
import com.csye6225.webservice.RESTfulWebService.Service.UserService;
import com.csye6225.webservice.RESTfulWebService.Validation.EmailValidator;
import com.csye6225.webservice.RESTfulWebService.Validation.PasswordValidator;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.timgroup.statsd.StatsDClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
public class ApplicationController {

    private final UserService userService;

    private EmailValidator emailValidator = new EmailValidator();

    private PasswordValidator passwordValidator = new PasswordValidator();

    private Logger logger = LoggerFactory.getLogger(ApplicationController.class);

    @Autowired
    private StatsDClient statsDClient;

    public ApplicationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/v1/helloworld")
    public String helloWorld() {
        statsDClient.incrementCounter("endpoint.helloworld.http.get");
        logger.info("This is a INFO level log.");
        logger.warn("This is a WARN level log");
        logger.error("This is a ERROR level log");
        return "Hello World";
    }

    @GetMapping("/users")
    public MappingJacksonValue findAll() {

        statsDClient.incrementCounter("endpoint.users.http.get");

        List<User> users = userService.findAll();

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.
                filterOutAllExcept("id", "username", "first_name", "last_name",
                        "email", "account_created", "account_updated");

        FilterProvider filters = new SimpleFilterProvider().addFilter("UserFilter", filter);

        MappingJacksonValue mapping = new MappingJacksonValue(users);

        mapping.setFilters(filters);

        return mapping;
    }

    @PostMapping("/v1/user")
    @ResponseStatus(HttpStatus.CREATED)
    public MappingJacksonValue createUser(@RequestBody User user) {

        statsDClient.incrementCounter("endpoint.user.http.post");

        String username = user.getEmail_address();

        // check whether the username is valid
        if (!emailValidator.isValid(username, null)) {
            logger.warn("Invalid email!");
            throw new EmailInvalidException("Invalid email!");
        }

        // check whether the password is strong
        String password = user.getPassword();
        if (password.length() < 8 || !passwordValidator.isValid(password)) {
            throw new WeakPasswordException("Password Too Weak!");
        }

        logger.info("Processing registration form for: " + username);

        // check the database if user already exists
        User existing = userService.findByUsername(username);
        if (existing != null){
            logger.warn("Username already exist!");
            throw new UserAlreadyExist("Username already exist!");
        }

        // set user attributes
        user.setId(UUID.randomUUID().toString());  // user id
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");  // set the date format
        user.setAccount_created(dateFormat.format(new Date()));
        User savedUser = userService.save(user);

        // set basic filter, avoid returning sensitive info such as password
        SimpleBeanPropertyFilter filter =
                SimpleBeanPropertyFilter.filterOutAllExcept("id", "first_name", "last_name",
                        "email_address", "account_created", "account_updated");

        FilterProvider filters = new SimpleFilterProvider().addFilter("UserFilter", filter);

        MappingJacksonValue mapping = new MappingJacksonValue(savedUser);

        mapping.setFilters(filters);

        return mapping;
    }

    @GetMapping("/v1/user/self")
    public MappingJacksonValue findOne() {

        statsDClient.incrementCounter("endpoint.user.http.get");

        User user = getCurrentUser();

        SimpleBeanPropertyFilter filter =
                SimpleBeanPropertyFilter.filterOutAllExcept("id", "first_name", "last_name",
                        "email_address", "account_created", "account_updated");

        FilterProvider filters = new SimpleFilterProvider().addFilter("UserFilter", filter);

        MappingJacksonValue mapping = new MappingJacksonValue(user);

        mapping.setFilters(filters);

        return mapping;
    }

    @PutMapping("/v1/user/self")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody User user) {

        statsDClient.incrementCounter("endpoint.user.http.put");

        if (user.getEmail_address() != null
                || user.getAccount_created() != null
                || user.getAccount_updated() != null) {
            throw new FieldRestrictedException("Fields not allowed to modify; " +
                    "Only can change FirstName, LastName and Password.");
        }

        String password = user.getPassword();
        if (password != null && (password.length() < 8 || !passwordValidator.isValid(password))) {
            throw new WeakPasswordException("Password Too Weak!");
        }

        User old = getCurrentUser();

        // pass those static attributes
        user.setId(old.getId());
        user.setEmail_address(old.getEmail_address());
        user.setAccount_created(old.getAccount_created());

        // save the composed user
        userService.save(user);
    }

    // helper function to get current authenticated user
    private User getCurrentUser() {

        // Get current authenticated user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.findByUsername(username);

        logger.info("Successfully obtained user: " + username);

        if (user == null) {
            throw new UserNotFoundException("User Not Found!");
        }

        return user;
    }

}
