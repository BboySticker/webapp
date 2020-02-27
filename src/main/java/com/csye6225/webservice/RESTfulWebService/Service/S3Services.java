package com.csye6225.webservice.RESTfulWebService.Service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface S3Services {

    void uploadFile(String keyName, MultipartFile file) throws IOException;

    void deleteFile(String keyName);

}
