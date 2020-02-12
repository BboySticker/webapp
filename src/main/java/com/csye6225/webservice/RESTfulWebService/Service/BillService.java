package com.csye6225.webservice.RESTfulWebService.Service;

import com.csye6225.webservice.RESTfulWebService.Entity.Bill;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BillService {

    Bill findById(String id);

    List<Bill> findAll(String userId);

    void deleteById(String id);

    Bill save(Bill bill);

}
