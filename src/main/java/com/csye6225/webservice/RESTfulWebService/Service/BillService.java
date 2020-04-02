package com.csye6225.webservice.RESTfulWebService.Service;

import com.csye6225.webservice.RESTfulWebService.Entity.Bill.Bill;

import java.util.List;

public interface BillService {

    List<String> getBillsDue(String recordId);

    String getBillsDue(String userId, int numOfDays);

    Bill findById(String id);

    List<Bill> findAll(String userId);

    void deleteById(String id);

    Bill save(Bill bill);

}
