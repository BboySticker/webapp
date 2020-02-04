package com.csye6225.webservice.RESTfulWebService.Dao;

import com.csye6225.webservice.RESTfulWebService.Entity.Bill;

import java.util.List;

public interface BillDao {

    Bill findById(String id);

    List<Bill> findAll(String userId);

    void deleteById(String id);

    void save(Bill bill);

}
