package com.csye6225.webservice.RESTfulWebService.Dao;

import com.csye6225.webservice.RESTfulWebService.Entity.Bill;
import com.csye6225.webservice.RESTfulWebService.Entity.File;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    public File store(String billId, MultipartFile file) throws IOException {

        Bill theBill = billDao.findById(billId);

        String fileId = UUID.randomUUID().toString();

        theBill.setAttachmentId(fileId);

        Session currentSession = sessionFactory.getCurrentSession();

        File theFile = new File();

        theFile.setId(fileId);
        theFile.setFileName(file.getOriginalFilename());
//        theFile.setUrl(file.getResource().getURL().toString());
        theFile.setUploadDate(new Date());

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

        Session currentSession = sessionFactory.getCurrentSession();

        Query theQuery =
                currentSession.createQuery("delete from File where id=:uFileId");

        theQuery.setParameter("uFileId", fileId);

        theQuery.executeUpdate();
    }
}
