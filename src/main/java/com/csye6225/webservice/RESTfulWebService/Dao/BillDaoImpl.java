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
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Bill findById(String id) {

        // get the current hibernate session
        Session currentSession = sessionFactory.getCurrentSession();

        Bill theBill = currentSession.get(Bill.class, id);

        return theBill;

    }

    @Override
    public List<Bill> findAll() {

        Session currentSession = sessionFactory.getCurrentSession();

        // now retrieve/read from database using username
        Query<Bill> theQuery =
                currentSession.createQuery("from Bill", Bill.class);

        List<Bill> users = theQuery.getResultList();

        return users;

    }

    // functional
    @Override
    public void deleteById(String id) {

        Session currentSession = sessionFactory.getCurrentSession();

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
