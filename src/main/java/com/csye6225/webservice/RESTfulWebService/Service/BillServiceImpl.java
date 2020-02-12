package com.csye6225.webservice.RESTfulWebService.Service;

import com.csye6225.webservice.RESTfulWebService.Dao.BillDao;
import com.csye6225.webservice.RESTfulWebService.Entity.Bill;
import com.csye6225.webservice.RESTfulWebService.Entity.File;
import com.csye6225.webservice.RESTfulWebService.Exception.StorageFileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

@Service
public class BillServiceImpl implements BillService {

    @Autowired
    private BillDao billDao;

    @Override
    @Transactional
    public Bill findById(String id) {
        return billDao.findById(id);
    }

    @Override
    @Transactional
    public List<Bill> findAll(String userId) {
        return billDao.findAll(userId);
    }

    @Override
    @Transactional
    public void deleteById(String id) {

        Bill theBill = billDao.findById(id);

        String fileId = theBill.getAttachment().getId();

        // delete the dir and the real file stored in file system
        File theFile = billDao.getFile(fileId);

        if (theFile == null) {
            throw new StorageFileNotFoundException("File Not Found!");
        }

        Path dirPath = Paths.get(theFile.getUrl());
        Path thePath = Paths.get(theFile.getUrl() + "/" + theFile.getFileName());

        System.out.println(thePath);

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

        billDao.deleteById(id);
    }

    @Override
    @Transactional
    public Bill save(Bill bill) {
        billDao.save(bill);
        return bill;
    }

}
