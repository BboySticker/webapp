package com.csye6225.webservice.RESTfulWebService.Service;

public interface SQSService {

    void putMessage(String recordId, String ownerId, String numOfDays);

}
