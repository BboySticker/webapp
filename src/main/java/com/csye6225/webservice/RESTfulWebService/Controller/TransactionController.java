package com.csye6225.webservice.RESTfulWebService.Controller;


import com.csye6225.webservice.RESTfulWebService.Entity.Bill;
import com.csye6225.webservice.RESTfulWebService.Entity.User;
import com.csye6225.webservice.RESTfulWebService.Exception.BillNotFoundException;
import com.csye6225.webservice.RESTfulWebService.Exception.UserNotFoundException;
import com.csye6225.webservice.RESTfulWebService.Service.BillService;
import com.csye6225.webservice.RESTfulWebService.Service.UserService;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;


@RestController
public class TransactionController {

    @Autowired
    private BillService billService;

    @Autowired
    private UserService userService;

    private Logger logger = Logger.getLogger(getClass().getName());

    @PostMapping("/v1/bill")
    @ResponseStatus(HttpStatus.CREATED)
    private @ResponseBody Bill createBill(@RequestBody Bill bill) {

        System.out.println(bill.getCategories());
        System.out.println(bill.getPaymentStatus());

        // use helper function to get current authenticated user
        User currentUser = getCurrentUser();

        // set those read-only attributes: id, createdTs, updatedTs, ownerId
        bill.setId(UUID.randomUUID().toString());
        bill.setCreatedTs(new Date());
        bill.setUpdatedTs(new Date());
        bill.setOwnerId(currentUser.getId());

        // save the bill
        Bill savedBill = billService.save(bill);

//        return applyFilter(savedBill);
        return savedBill;
    }

    @GetMapping("/v1/bills")
    public @ResponseBody List<Bill> getAllBills() {

        User currentUser = getCurrentUser();
        List<Bill> bills = billService.findAll(currentUser.getId());
        return bills;
    }

    @GetMapping("/v1/bill/{id}")
    private Bill getBill(@PathVariable String id) {

        User currentUser = getCurrentUser();

        // get the bill by id
        Bill theBill = billService.findById(id);

        if (theBill == null || ! theBill.getOwnerId().equals(currentUser.getId())) {
            throw new BillNotFoundException("Bill Not Found!");
        }
//        return applyFilter(theBill);
        return theBill;
    }

    @DeleteMapping("/v1/bill/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private void deleteBill(@PathVariable String id) {

        User currentUser = getCurrentUser();

        logger.info("Deleting the item which id: " + id);

        Bill theBill = billService.findById(id);

        // throw exception when:
        // 1. passed id not exist;
        // 2. ownerId field in bill object is null;
        // 3. bill's ownerId is not equal to currentUser's id
        if (theBill == null || theBill.getOwnerId() == null ||
                ! theBill.getOwnerId().equals(currentUser.getId())) {
            throw new BillNotFoundException("Bill Not Found!");
        }
        billService.deleteById(id);
    }

    @PutMapping("/v1/bill/{id}")
    private @ResponseBody Bill updateBill(@RequestBody Bill bill, @PathVariable String id) {

        User currentUser = getCurrentUser();

        Bill theBill = billService.findById(id);

        if (theBill == null || ! currentUser.getId().equals(theBill.getOwnerId())) {
            throw new BillNotFoundException("Bill Not Found!");
        }

        // pass those four read-only fields
        bill.setId(id);
        bill.setCreatedTs(theBill.getCreatedTs());
        bill.setUpdatedTs(new Date());
        bill.setOwnerId(theBill.getOwnerId());

        billService.save(bill);

//        return applyFilter(bill);
        return bill;
    }

    // helper function to get current authenticated user
    private User getCurrentUser() {

        // Get current authenticated user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.findByUsername(username);

        logger.info("Successfully obtained user: " + username);

        if (user == null) {
            throw new UserNotFoundException("User Not Found!");
        }

        return user;
    }

    // two helper function, apply filter on Bill object to filter out those sensitive attributes
    private MappingJacksonValue applyFilter(Bill bill) {

        SimpleBeanPropertyFilter filter =
                SimpleBeanPropertyFilter.filterOutAllExcept(
                        "id", "createdTs", "updatedTs", "ownerId", "vendor",
                        "billDate", "dueDate", "amountDue", "categories", "paymentStatus");

        FilterProvider filters = new SimpleFilterProvider().addFilter("BillFilter", filter);

        MappingJacksonValue mapping = new MappingJacksonValue(bill);

        mapping.setFilters(filters);

        return mapping;
    }

    private MappingJacksonValue applyFilter(List<Bill> bills) {

        SimpleBeanPropertyFilter filter =
                SimpleBeanPropertyFilter.filterOutAllExcept(
                        "id", "createdTs", "updatedTs", "ownerId", "vendor",
                        "billDate", "dueDate", "amountDue", "categories", "paymentStatus");

        FilterProvider filters = new SimpleFilterProvider().addFilter("BillFilter", filter);

        MappingJacksonValue mapping = new MappingJacksonValue(bills);

        mapping.setFilters(filters);

        return mapping;
    }

}
