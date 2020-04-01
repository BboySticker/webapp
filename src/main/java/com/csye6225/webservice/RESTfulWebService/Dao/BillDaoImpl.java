package com.csye6225.webservice.RESTfulWebService.Dao;

import com.csye6225.webservice.RESTfulWebService.Entity.Bill.Bill;
import com.csye6225.webservice.RESTfulWebService.Entity.Bill.DueBillRecord;
import com.csye6225.webservice.RESTfulWebService.Entity.Bill.File;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public class BillDaoImpl implements BillDao {

    // inject the session factory
    private SessionFactory sessionFactory;

    private Logger logger = LogManager.getLogger(getClass());

    @Autowired
    public BillDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public String getBillsDue(String userId, int numOfDays) {

        List<Bill> bills = this.findAll(userId);
        List<String> dueBills = new ArrayList<>();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date today = new Date();
        logger.info("Start obtain due bills of user: " + userId + "; within " + numOfDays + " days.");
        logger.info("Today: " + today.toString());
        logger.info("Today's timestamp: " + today.getTime());
//        Date testDueDate = new Date();
//        try {
//            testDueDate = dateFormat.parse("2020-04-15");
//        } catch (ParseException ex) {
//            ex.printStackTrace();
//        }
//        logger.info("Test bill due date: " + testDueDate.toString());
//        logger.info("Test bill due date timestamp: " + testDueDate.getTime());
        try {
            for (Bill bill: bills) {
                Date date = dateFormat.parse(bill.getDueDate());
                if (((date.getTime() - today.getTime()) / (24 * 60 * 60 * 1000)) > 0
                        && ((date.getTime() - today.getTime()) / (24 * 60 * 60 * 1000)) <= numOfDays) {
                    dueBills.add(bill.getId());
                    logger.info("Successfully obtained due bill: " + bill.getId());
                }
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        logger.info("Finish obtain due bills of user: " + userId + "; within " + numOfDays + " days.");
        if (dueBills.size() == 0) {
            logger.info("No due bills for user: " + userId + " within " + numOfDays + " days.");
            return "N/A";
        }
        DueBillRecord dueBillRecord = new DueBillRecord();
        dueBillRecord.setId(UUID.randomUUID().toString());
        dueBillRecord.setOwnerId(userId);
        dueBillRecord.setDueBills(dueBills);

        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.save(dueBillRecord);
        return dueBillRecord.getId();
    }

    @Override
    public Bill findById(String id) {

        // get the current hibernate session
        Session currentSession = sessionFactory.getCurrentSession();

        Bill theBill = currentSession.get(Bill.class, id);

        return theBill;
    }

    @Override
    public List<Bill> findAll(String userId) {

        Session currentSession = sessionFactory.getCurrentSession();

        // now retrieve/read from database using username
        Query<Bill> theQuery =
                currentSession.createQuery("from Bill where owner_id=:uId", Bill.class);

        theQuery.setParameter("uId", userId);

        List<Bill> bills = theQuery.getResultList();

        return bills;
    }

    @Override
    public void deleteById(String id) {

        Session currentSession = sessionFactory.getCurrentSession();

        // delete the file attached to this bill
        Bill theBill = findById(id);

        String fileId = theBill.getAttachment().getId();

        System.out.println("Delete the attachment: " + fileId);

        // delete the bill
        // it can delete the attached file automatically
        currentSession.delete(theBill);
    }

    @Override
    public void save(Bill bill) {

        Session currentSession = sessionFactory.getCurrentSession();

        currentSession.saveOrUpdate(bill);
    }

    public File getFile(String fileId) {

        Session currentSession = sessionFactory.getCurrentSession();

        File theFile = currentSession.get(File.class, fileId);

        return theFile;
    }

}
