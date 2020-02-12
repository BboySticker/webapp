package com.csye6225.webservice.RESTfulWebService.Service;

import com.csye6225.webservice.RESTfulWebService.Configuration.StorageProperties;
import com.csye6225.webservice.RESTfulWebService.Dao.StorageDao;
import com.csye6225.webservice.RESTfulWebService.Entity.File;
import com.csye6225.webservice.RESTfulWebService.Exception.StorageException;
import com.csye6225.webservice.RESTfulWebService.Exception.StorageFileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;


@Service
public class StorageServiceImpl implements StorageService {

    private final Path rootLocation;
    private StorageDao storageDao;
    private StorageProperties properties;

    @Autowired
    public StorageServiceImpl(StorageProperties properties, StorageDao storageDao, UserService userService) {
        this.rootLocation = Paths.get(properties.getLocation());
        this.properties = properties;
        this.storageDao = storageDao;
    }

    @Override
    @Transactional
    public File store(String billId, MultipartFile file) throws IOException {

        String filename = StringUtils.cleanPath(file.getOriginalFilename());

        System.out.println("Inside Service: " + filename);

        Path location = Paths.get(properties.getLocation() + billId);

        location.toFile().mkdir();

        System.out.println("Full Path: " + location.toFile().getAbsolutePath());

        // attach the file to this bill
        File theFile = storageDao.store(billId, file, location);

        // store the physical file into server
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
                Files.copy(inputStream, location.resolve(filename),
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

        File theFile = storageDao.findById(fileId);

        if (theFile == null) {
            throw new StorageFileNotFoundException("File Not Found!");
        }

        Path dirPath = Paths.get(theFile.getUrl());
        Path thePath = Paths.get(theFile.getUrl() + "/" + theFile.getFileName());

        System.out.println(thePath);

        // try to delete the physical file from server
        try {
            Files.deleteIfExists(thePath);

            dirPath.toFile().delete();
        }
        catch(NoSuchFileException e) {
            System.out.println("No such file/directory exists");
        }
        catch(DirectoryNotEmptyException e) {
            System.out.println("Directory is not empty.");
        }
        catch(IOException e) {
            System.out.println("Invalid permissions.");
        }

        System.out.println("Deletion successful.");

        // delete from database
        storageDao.deleteById(fileId);
    }

}
