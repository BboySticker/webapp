package com.csye6225.webservice.RESTfulWebService.Controller;


import com.csye6225.webservice.RESTfulWebService.Entity.Bill;
import com.csye6225.webservice.RESTfulWebService.Entity.User;
import com.csye6225.webservice.RESTfulWebService.Exception.BillNotFoundException;
import com.csye6225.webservice.RESTfulWebService.Exception.SuccessfullyDeleted;
import com.csye6225.webservice.RESTfulWebService.Exception.UserNotFoundException;
import com.csye6225.webservice.RESTfulWebService.Service.BillService;
import com.csye6225.webservice.RESTfulWebService.Service.UserService;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;


@RestController
public class TransactionController {

    @Autowired
    private BillService billService;

    private Logger logger = Logger.getLogger(getClass().getName());

    // not functional
    @PostMapping("/v1/bill")
    private void createBill(@RequestBody Bill bill) {

        billService.save(bill);

    }

    // functional
    @GetMapping("/v1/bills")
    public MappingJacksonValue getAllBills() {

        List<Bill> bills = billService.findAll();

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.
                filterOutAllExcept("id");

        FilterProvider filters = new SimpleFilterProvider().addFilter("BillFilter", filter);

        MappingJacksonValue mapping = new MappingJacksonValue(bills);

        mapping.setFilters(filters);

        return mapping;
    }

    // functional
    @GetMapping("/v1/bill/{id}")
    private MappingJacksonValue getBill(@PathVariable String id) {

        Bill theBill = billService.findById(id);

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.
                filterOutAllExcept("id", "created_ts", "updated_ts");

        FilterProvider filters = new SimpleFilterProvider().addFilter("BillFilter", filter);

        MappingJacksonValue mapping = new MappingJacksonValue(theBill);

        mapping.setFilters(filters);

        return mapping;

    }

    @DeleteMapping("/v1/bill/{id}")
    private void deleteBill(@PathVariable String id) {

        logger.info("Deleting the item which id: " + id);

        Bill theBill = billService.findById(id);

        if (theBill == null) {
            throw new BillNotFoundException("Bill Not Found!");
        }

        billService.deleteById(id);

        throw new SuccessfullyDeleted("Successfully Deleted!");

    }

    @PutMapping("/v1/bill/{id}")
    private MappingJacksonValue updateBill(@RequestBody Bill bill, @PathVariable String id) {

        bill.setId(id);

        billService.save(bill);

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.
                filterOutAllExcept("id");

        FilterProvider filters = new SimpleFilterProvider().addFilter("BillFilter", filter);

        MappingJacksonValue mapping = new MappingJacksonValue(bill);

        mapping.setFilters(filters);

        return mapping;

    }

}
