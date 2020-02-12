package com.csye6225.webservice.RESTfulWebService.Dao;

import com.csye6225.webservice.RESTfulWebService.Entity.Bill;
import com.csye6225.webservice.RESTfulWebService.Entity.File;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Date;
import java.util.UUID;

@Repository
public class StorageDaoImpl implements StorageDao {

    private SessionFactory sessionFactory;
    private BillDao billDao;

    @Autowired
    public StorageDaoImpl(SessionFactory sessionFactory, BillDao billDao) {
        this.sessionFactory = sessionFactory;
        this.billDao = billDao;
    }

    @Override
    public File store(String billId, MultipartFile file, Path location) throws IOException {

        Bill theBill = billDao.findById(billId);

        String fileId = UUID.randomUUID().toString();

        Session currentSession = sessionFactory.getCurrentSession();

        File theFile = new File();

        theFile.setId(fileId);
        theFile.setFileName(file.getOriginalFilename());
        theFile.setUrl(location.toFile().getAbsolutePath());
        theFile.setUploadDate(new Date());

        theFile.setBillId(theBill.getId());
        theFile.setOwnerId(theBill.getOwnerId());
        theFile.setSize(file.getSize());

        theBill.setAttachment(theFile);

        currentSession.saveOrUpdate(theFile);

        currentSession.saveOrUpdate(theBill);

        return theFile;
    }

    @Override
    public File findById(String fileId) {

        Session currentSession = sessionFactory.getCurrentSession();

        File theFile = currentSession.get(File.class, fileId);

        return theFile;
    }

    @Override
    public void deleteById(String fileId) {

        File theFile = findById(fileId);

        Bill theBill = billDao.findById(theFile.getBillId());

        theBill.setAttachment(null);

        billDao.save(theBill);

        Session currentSession = sessionFactory.getCurrentSession();

        currentSession.delete(theFile);
    }

}
