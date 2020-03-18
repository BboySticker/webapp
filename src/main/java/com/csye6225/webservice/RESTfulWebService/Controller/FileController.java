package com.csye6225.webservice.RESTfulWebService.Controller;

import com.csye6225.webservice.RESTfulWebService.Entity.Bill.Bill;
import com.csye6225.webservice.RESTfulWebService.Entity.Bill.File;
import com.csye6225.webservice.RESTfulWebService.Entity.User.User;
import com.csye6225.webservice.RESTfulWebService.Exception.AttachFileException;
import com.csye6225.webservice.RESTfulWebService.Exception.BillNotFoundException;
import com.csye6225.webservice.RESTfulWebService.Exception.StorageFileNotFoundException;
import com.csye6225.webservice.RESTfulWebService.Exception.UserNotFoundException;
import com.csye6225.webservice.RESTfulWebService.Service.BillService;
import com.csye6225.webservice.RESTfulWebService.Service.StorageService;
import com.csye6225.webservice.RESTfulWebService.Service.UserService;
import com.timgroup.statsd.StatsDClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;


@RestController
public class FileController {

    private static final String[] extensions = {"pdf", "jpeg", "jpg", "png"};
    private static final List EXTENSIONS = Arrays.asList(extensions);

    private StorageService storageService;
    private BillService billService;
    private UserService userService;

    private Logger logger = LogManager.getLogger(getClass());

    @Autowired
    private StatsDClient statsDClient;

    @Autowired
    public FileController(StorageService storageService, BillService billService, UserService userService) {
        this.storageService = storageService;
        this.billService = billService;
        this.userService = userService;
    }

    @PostMapping("/v1/bill/{id}/file")
    private @ResponseBody File attachFile(@PathVariable String id, @RequestParam("file") MultipartFile file) throws IOException {

        long apiStartTime = System.currentTimeMillis();
        statsDClient.incrementCounter("endpoint.file.http.post");

        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        String[] parts = filename.split("\\.");
        String suffix = parts[parts.length - 1];

        logger.info("Uploading file... File Name: " + filename);
        logger.info("Upload File Extension is: " + suffix);

        // file not allowed to store
        if (! EXTENSIONS.contains(suffix)) {
            logger.error("File error");
            throw new AttachFileException("Attach File Error!");
        }

        Bill theBill = billService.findById(id);
        if (theBill == null || ! theBill.getOwnerId().equals(getCurrentUser().getId())) {
            logger.error("Bill not found");
            throw new AttachFileException("Bill Not Found!");
        }

        File attachment = theBill.getAttachment();
        if (attachment != null) {
            storageService.deleteById(attachment.getId());
        }

        // count the time of db call
        long startTime = System.currentTimeMillis();
        File theFile = storageService.store(id, file);
        long endTime = System.currentTimeMillis();
        statsDClient.recordExecutionTime("db.ops.endpoint.file.http.post", endTime - startTime);

        // count the time of api call
        long apiEndTime = System.currentTimeMillis();
        statsDClient.recordExecutionTime("api.ops.endpoint.file.http.post", apiEndTime - apiStartTime);
        return theFile;
    }

    @GetMapping("/v1/bill/{id}/file/{fileId}")
    @ResponseStatus(HttpStatus.OK)
    private @ResponseBody File getFile(@PathVariable String id, @PathVariable String fileId) {

        long apiStartTime = System.currentTimeMillis();
        statsDClient.incrementCounter("endpoint.file.http.get");

        Bill theBill = billService.findById(id);
        // count the time of db call
        long startTime = System.currentTimeMillis();
        File theFile = storageService.findById(fileId);
        long endTime = System.currentTimeMillis();
        statsDClient.recordExecutionTime("db.ops.endpoint.file.http.get", endTime - startTime);

        // current user id == bill owner id
        // bill attachment id == file id
        if (theBill == null || ! theBill.getOwnerId().equals(getCurrentUser().getId())) {
            logger.error("File not found");
            throw new BillNotFoundException("Bill Not Found!");
        }
        if (theFile == null || theBill.getAttachment() == null
                || theBill.getAttachment().getId() == null
                || ! theBill.getAttachment().getId().equals(theFile.getId())) {
            logger.warn("File not found");
            throw new StorageFileNotFoundException("File Not Found");
        }
        // count the time of api call
        long apiEndTime = System.currentTimeMillis();
        statsDClient.recordExecutionTime("api.ops.endpoint.file.http.get", apiEndTime - apiStartTime);
        return theFile;
    }

    @DeleteMapping("/v1/bill/{id}/file/{fileId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private void delete(@PathVariable String id, @PathVariable String fileId) {

        // 1. delete file from the database
        // 2. delete the attachment id inside bill database
        // 3. delete the physical file from server
        // 4. when delete a bill, the attached file should be deleted
        long apiStartTime = System.currentTimeMillis();
        statsDClient.incrementCounter("endpoint.file.http.delete");
        logger.info("Deleting file... Bill ID: " + id + " File ID: " + fileId);

        // count the time of db call
        long startTime = System.currentTimeMillis();
        storageService.deleteById(fileId);
        long endTime = System.currentTimeMillis();
        statsDClient.recordExecutionTime("db.ops.endpoint.file.http.delete", endTime - startTime);

        // count the time of api call
        long apiEndTime = System.currentTimeMillis();
        statsDClient.recordExecutionTime("api.ops.endpoint.file.http.delete", apiEndTime - apiStartTime);
    }

    // helper function to get current authenticated user
    private User getCurrentUser() {

        // Get current authenticated user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.findByUsername(username);

        logger.info("Successfully obtained user: " + username);

        if (user == null) {
            throw new UserNotFoundException("User Not Found!");
        }
        return user;
    }

}
