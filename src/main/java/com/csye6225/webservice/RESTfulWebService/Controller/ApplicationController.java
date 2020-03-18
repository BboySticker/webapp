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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    private Logger logger = LogManager.getLogger(getClass());

    @Autowired
    private StatsDClient statsDClient;

    public ApplicationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/v1/helloworld")
    public String helloWorld() {
        statsDClient.incrementCounter("endpoint.helloworld.http.get");
        logger.info("This is a INFO level log");
        logger.warn("This is a WARN level log");
        logger.error("This is a ERROR level log");
        return "Hello World";
    }

    @GetMapping("/v1/users")
    public MappingJacksonValue findAll() {

        statsDClient.incrementCounter("endpoint.users.http.get");
        logger.info("Retrieving all users");

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

        long apiStartTime = System.currentTimeMillis();
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
            logger.warn("Password too weak");
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

        long startTime = System.currentTimeMillis();
        User savedUser = userService.save(user);
        long endTime = System.currentTimeMillis();
        statsDClient.recordExecutionTime("db.ops.endpoint.user.http.post", endTime - startTime);

        logger.info("Creating user... ID: " + savedUser.getId());

        // set basic filter, avoid returning sensitive info such as password
        SimpleBeanPropertyFilter filter =
                SimpleBeanPropertyFilter.filterOutAllExcept("id", "first_name", "last_name",
                        "email_address", "account_created", "account_updated");

        FilterProvider filters = new SimpleFilterProvider().addFilter("UserFilter", filter);
        MappingJacksonValue mapping = new MappingJacksonValue(savedUser);
        mapping.setFilters(filters);

        long apiEndTime = System.currentTimeMillis();
        statsDClient.recordExecutionTime("api.ops.endpoint.user.http.post", apiEndTime - apiStartTime);

        return mapping;
    }

    @GetMapping("/v1/user/self")
    public MappingJacksonValue findOne() {

        long apiStartTime = System.currentTimeMillis();
        statsDClient.incrementCounter("endpoint.user.http.get");
        logger.info("Retrieving current user");

        long startTime = System.currentTimeMillis();
        User user = getCurrentUser();
        long endTime = System.currentTimeMillis();
        statsDClient.recordExecutionTime("db.ops.endpoint.user.http.get", endTime - startTime);

        SimpleBeanPropertyFilter filter =
                SimpleBeanPropertyFilter.filterOutAllExcept("id", "first_name", "last_name",
                        "email_address", "account_created", "account_updated");

        FilterProvider filters = new SimpleFilterProvider().addFilter("UserFilter", filter);
        MappingJacksonValue mapping = new MappingJacksonValue(user);
        mapping.setFilters(filters);

        long apiEndTime = System.currentTimeMillis();
        statsDClient.recordExecutionTime("api.ops.endpoint.user.http.get", apiEndTime - apiStartTime);
        return mapping;
    }

    @PutMapping("/v1/user/self")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody User user) {

        long apiStartTime = System.currentTimeMillis();
        statsDClient.incrementCounter("endpoint.user.http.put");
        logger.info("Updating user...");

        if (user.getEmail_address() != null
                || user.getAccount_created() != null
                || user.getAccount_updated() != null) {
            logger.warn("Updating user: Fields are not allowed to modify");
            throw new FieldRestrictedException("Fields not allowed to modify; " +
                    "Only can change FirstName, LastName and Password.");
        }
        String password = user.getPassword();
        if (password != null && (password.length() < 8 || !passwordValidator.isValid(password))) {
            logger.warn("Password too weak");
            throw new WeakPasswordException("Password Too Weak!");
        }
        User old = getCurrentUser();
        // pass those static attributes
        user.setId(old.getId());
        user.setEmail_address(old.getEmail_address());
        user.setAccount_created(old.getAccount_created());

        long startTime = System.currentTimeMillis();
        // save the composed user
        userService.save(user);
        long endTime = System.currentTimeMillis();
        statsDClient.recordExecutionTime("db.ops.endpoint.user.http.put", endTime - startTime);

        logger.info("User updated. ID: " + old.getId());

        long apiEndTime = System.currentTimeMillis();
        statsDClient.recordExecutionTime("api.ops.endpoint.user.http.put", apiEndTime - apiStartTime);
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
