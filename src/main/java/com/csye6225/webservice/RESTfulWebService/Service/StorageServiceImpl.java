package com.csye6225.webservice.RESTfulWebService.Service;

import com.csye6225.webservice.RESTfulWebService.Configuration.StorageProperties;
import com.csye6225.webservice.RESTfulWebService.Dao.StorageDao;
import com.csye6225.webservice.RESTfulWebService.Entity.File;
import com.csye6225.webservice.RESTfulWebService.Exception.StorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


@Service
public class StorageServiceImpl implements StorageService {

    private final Path rootLocation;
    private StorageDao storageDao;

    @Autowired
    public StorageServiceImpl(StorageProperties properties, StorageDao storageDao) {
        this.rootLocation = Paths.get(properties.getLocation());
        this.storageDao = storageDao;
    }

    @Override
    @Transactional
    public File store(String billId, MultipartFile file) throws IOException {

        // attach the file to this bill
        File theFile = storageDao.store(billId, file);

        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        System.out.println("Inside Service: " + filename);
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new StorageException(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, this.rootLocation.resolve(filename),
                        StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }

        return theFile;
    }

    @Transactional
    public File findById(String fileId) {

        return storageDao.findById(fileId);

    }

    @Transactional
    public void deleteById(String fileId) {

        storageDao.deleteById(fileId);

    }

}
