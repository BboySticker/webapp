package com.csye6225.webservice.RESTfulWebService.Controller;

import com.csye6225.webservice.RESTfulWebService.Entity.Bill;
import com.csye6225.webservice.RESTfulWebService.Entity.File;
import com.csye6225.webservice.RESTfulWebService.Entity.User;
import com.csye6225.webservice.RESTfulWebService.Exception.AttachFileException;
import com.csye6225.webservice.RESTfulWebService.Exception.BillNotFoundException;
import com.csye6225.webservice.RESTfulWebService.Exception.StorageFileNotFoundException;
import com.csye6225.webservice.RESTfulWebService.Exception.UserNotFoundException;
import com.csye6225.webservice.RESTfulWebService.Service.BillService;
import com.csye6225.webservice.RESTfulWebService.Service.StorageService;
import com.csye6225.webservice.RESTfulWebService.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.logging.Logger;


@RestController
public class FileController {

    private StorageService storageService;
    private BillService billService;
    private UserService userService;

    private Logger logger = Logger.getLogger(getClass().getName());

    @Autowired
    public FileController(StorageService storageService, BillService billService, UserService userService) {
        this.storageService = storageService;
        this.billService = billService;
        this.userService = userService;
    }

    @PostMapping("/v1/bill/{id}/file")
    private @ResponseBody File attachFile(@PathVariable String id, @RequestParam("file") MultipartFile file) throws IOException {

        Bill theBill = billService.findById(id);

        if (theBill == null || ! theBill.getOwnerId().equals(getCurrentUser().getId())) {
            throw new AttachFileException("Bill Not Found!");
        }

        File attachment = theBill.getAttachment();

        if (attachment != null) {
            storageService.deleteById(attachment.getId());
        }

        File theFile = storageService.store(id, file);

        return theFile;
    }

    @GetMapping("/v1/bill/{id}/file/{fileId}")
    @ResponseStatus(HttpStatus.OK)
    private @ResponseBody File getFile(@PathVariable String id, @PathVariable String fileId) {

        Bill theBill = billService.findById(id);
        File theFile = storageService.findById(fileId);

        // current user id == bill owner id
        // bill attachment id == file id
        if (theBill == null || ! theBill.getOwnerId().equals(getCurrentUser().getId())) {
            throw new BillNotFoundException("Bill Not Found!");
        }
        if (theFile == null || theBill.getAttachment().getId() == null
                || ! theBill.getAttachment().getId().equals(theFile.getId())) {
            throw new StorageFileNotFoundException("File Not Found");
        }
        return theFile;
    }

    @DeleteMapping("/v1/bill/{id}/file/{fileId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private void delete(@PathVariable String id, @PathVariable String fileId) {

        // 1. delete file from the database
        // 2. delete the attachment id inside bill database
        // 3. delete the physical file from server
        // 4. when delete a bill, the attached file should be deleted

        storageService.deleteById(fileId);
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
