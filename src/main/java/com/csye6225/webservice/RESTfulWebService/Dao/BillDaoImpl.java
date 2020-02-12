package com.csye6225.webservice.RESTfulWebService.Dao;

import com.csye6225.webservice.RESTfulWebService.Entity.Bill;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BillDaoImpl implements BillDao {

    // inject the session factory
    private SessionFactory sessionFactory;

    @Autowired
    public BillDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
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

        String fileId = theBill.getAttachmentId();

        System.out.println("Delete the attachment: " + fileId);

        // delete the attachment of this bill, if it exists
        Query deleteAttachment =
                currentSession.createQuery("delete from File where id=:uFileId");

        deleteAttachment.setParameter("uFileId", fileId);

        deleteAttachment.executeUpdate();

        // delete the bill
        Query theQuery =
                currentSession.createQuery("delete from Bill where id=:billId");

        theQuery.setParameter("billId", id);

        theQuery.executeUpdate();
    }

    @Override
    public void save(Bill bill) {

        Session currentSession = sessionFactory.getCurrentSession();

        currentSession.saveOrUpdate(bill);
    }

}
