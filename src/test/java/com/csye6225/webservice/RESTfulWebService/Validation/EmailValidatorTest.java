package com.csye6225.webservice.RESTfulWebService.Validation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailValidatorTest {

    EmailValidator emailValidator = new EmailValidator();

    @Test
    void test1() {
        assertEquals(false, emailValidator.isValid("12345678", null));
        assertEquals(false, emailValidator.isValid("abcdefgh", null));
        assertEquals(false, emailValidator.isValid("1", null));
        assertEquals(false, emailValidator.isValid("a", null));
    }

    @Test
    void test2() {
        assertEquals(true, emailValidator.isValid("zhang@gmail.com", null));
        assertEquals(true, emailValidator.isValid("bboy@qq.com", null));
        assertEquals(true, emailValidator.isValid("zhang@husky.neu.edu", null));
        assertEquals(true, emailValidator.isValid("zhang@northeastern.edu", null));
    }

}