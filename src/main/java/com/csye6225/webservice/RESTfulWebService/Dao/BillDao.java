package com.csye6225.webservice.RESTfulWebService.Dao;

import com.csye6225.webservice.RESTfulWebService.Entity.Bill.Bill;
import com.csye6225.webservice.RESTfulWebService.Entity.Bill.File;

import java.util.List;

public interface BillDao {

    Bill findById(String id);

    List<Bill> findAll(String userId);

    void deleteById(String id);

    void save(Bill bill);

    File getFile(String fileId);

}
