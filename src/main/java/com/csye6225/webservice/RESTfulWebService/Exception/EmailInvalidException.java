package com.csye6225.webservice.RESTfulWebService.Exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmailInvalidException extends RuntimeException {

    public EmailInvalidException(String message) {super(message);}

}
