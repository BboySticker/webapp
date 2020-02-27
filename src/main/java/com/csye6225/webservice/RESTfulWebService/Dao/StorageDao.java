package com.csye6225.webservice.RESTfulWebService.Dao;

import com.amazonaws.services.s3.model.PutObjectResult;
import com.csye6225.webservice.RESTfulWebService.Entity.Bill.File;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

public interface StorageDao {

    File store(String billId, MultipartFile file, Path location) throws IOException;

    File store(String billId, MultipartFile file, String keyName);

    File store(String billId, MultipartFile file, String keyName, PutObjectResult obj);

    File findById(String fileId);

    void deleteById(String fileId);

}
