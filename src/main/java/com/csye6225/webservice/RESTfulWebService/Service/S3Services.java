package com.csye6225.webservice.RESTfulWebService.Service;

import com.amazonaws.services.s3.model.PutObjectResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface S3Services {

    PutObjectResult uploadFile(String keyName, MultipartFile file) throws IOException;

    void deleteFile(String keyName);

}
