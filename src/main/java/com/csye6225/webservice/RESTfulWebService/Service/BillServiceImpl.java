package com.csye6225.webservice.RESTfulWebService.Service;

import com.csye6225.webservice.RESTfulWebService.Dao.BillDao;
import com.csye6225.webservice.RESTfulWebService.Entity.Bill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        billDao.deleteById(id);
    }

    @Override
    @Transactional
    public Bill save(Bill bill) {
        billDao.save(bill);
        return bill;
    }
}
