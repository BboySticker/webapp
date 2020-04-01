package com.csye6225.webservice.RESTfulWebService.Service;

import com.csye6225.webservice.RESTfulWebService.Dao.BillDao;
import com.csye6225.webservice.RESTfulWebService.Entity.Bill.Bill;
import com.csye6225.webservice.RESTfulWebService.Entity.Bill.File;
import com.csye6225.webservice.RESTfulWebService.Exception.StorageFileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

@Service
public class BillServiceImpl implements BillService {

    @Value("${app.profile.name}")
    private String PROFILE_NAME;

    @Autowired
    private BillDao billDao;

    @Autowired
    private S3Services s3Services;

    @Override
    @Transactional
    public String getBillsDue(String userId, int numOfDays) {
        return billDao.getBillsDue(userId, numOfDays);
    }

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

        // delete the file attached to this bill
        if (PROFILE_NAME.equalsIgnoreCase("local")) {

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
        }
        else if (PROFILE_NAME.equalsIgnoreCase("aws")) {

            s3Services.deleteFile(theFile.getUrl());

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
