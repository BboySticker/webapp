package com.csye6225.webservice.RESTfulWebService.Utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ConvertFromMultipart {

    public static File convertFromMultipart(MultipartFile file) throws FileNotFoundException, IOException {
        File newFile = new File(file.getOriginalFilename());
        newFile.createNewFile();
        FileOutputStream fs = new FileOutputStream(newFile);
        fs.write(file.getBytes());
        fs.close();
        return newFile;
    }

}
