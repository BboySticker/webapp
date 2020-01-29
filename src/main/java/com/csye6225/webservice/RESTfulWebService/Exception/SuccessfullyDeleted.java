package com.csye6225.webservice.RESTfulWebService.Exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NO_CONTENT)
public class SuccessfullyDeleted extends RuntimeException {

    public SuccessfullyDeleted(String message) {
        super(message);
    }

}
