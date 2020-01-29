package com.csye6225.webservice.RESTfulWebService.Validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordValidator {
    private Pattern pattern;
    private Matcher matcher;

    private static final String PASSWORD_PATTERN = "((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[!@#$^*+=]).{8,40})";

    public PasswordValidator() {
        pattern = Pattern.compile(PASSWORD_PATTERN);
    }

    public boolean isValid(final String password) {

        matcher = pattern.matcher(password);
        return matcher.matches();

    }
}
