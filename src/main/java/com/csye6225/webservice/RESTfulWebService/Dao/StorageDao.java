package com.csye6225.webservice.RESTfulWebService.Dao;

import com.csye6225.webservice.RESTfulWebService.Entity.File;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StorageDao {

    File store(String billId, MultipartFile file) throws IOException;

    File findById(String fileId);

    void deleteById(String fileId);

}
