package com.csye6225.webservice.RESTfulWebService.Controller;


import com.csye6225.webservice.RESTfulWebService.Entity.Bill.Bill;
import com.csye6225.webservice.RESTfulWebService.Entity.User.User;
import com.csye6225.webservice.RESTfulWebService.Exception.BillNotFoundException;
import com.csye6225.webservice.RESTfulWebService.Exception.UnauthorizedException;
import com.csye6225.webservice.RESTfulWebService.Exception.UserNotFoundException;
import com.csye6225.webservice.RESTfulWebService.Service.BillService;
import com.csye6225.webservice.RESTfulWebService.Service.SQSService;
import com.csye6225.webservice.RESTfulWebService.Service.UserService;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.timgroup.statsd.StatsDClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@RestController
public class TransactionController {

    @Autowired
    private BillService billService;

    @Autowired
    private SQSService sqsService;

    @Autowired
    private UserService userService;

    @Autowired
    private StatsDClient statsDClient;

    private Logger logger = LogManager.getLogger(getClass());

    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @GetMapping("/v1/bills/due/{days}")
    @ResponseStatus(HttpStatus.CREATED)
    private void getBillsDue(@PathVariable String days) {
        String recordId = billService.getBillsDue(getCurrentUser().getId(), Integer.parseInt(days));
        sqsService.putMessage(recordId, getCurrentUser().getEmail_address(), days);
    }

    @GetMapping("/v1/bills/{recordId}")
    private @ResponseBody List<Bill> getRecord(@PathVariable String recordId) {
        List<String> dueBillsId = billService.getBillsDue(recordId);
        if (dueBillsId == null) {
            throw new BillNotFoundException("Bill Not Found!");
        }
        List<Bill> dueBills = new ArrayList<>();
        for (String billId: dueBillsId) {
            dueBills.add(billService.findById(billId));
        }
        Bill firstBill = dueBills.get(0);
        if (! getCurrentUser().getId().equals(firstBill.getOwnerId())) {
            throw new UnauthorizedException("This user is unauthorized to access this resource!");
        }
        return dueBills;
    }

    @PostMapping("/v1/bill")
    @ResponseStatus(HttpStatus.CREATED)
    private @ResponseBody Bill createBill(@RequestBody Bill bill) {

        long apiStartTime = System.currentTimeMillis();
        statsDClient.incrementCounter("endpoint.bill.http.post");
        logger.info("Creating bill...");

        // use helper function to get current authenticated user
        User currentUser = getCurrentUser();
        // set those read-only attributes: id, createdTs, updatedTs, ownerId
        bill.setId(UUID.randomUUID().toString());
        bill.setCreatedTs(dateFormat.format(new Date()));
        bill.setUpdatedTs(dateFormat.format(new Date()));
        bill.setOwnerId(currentUser.getId());

        long startTime = System.currentTimeMillis();
        Bill savedBill = billService.save(bill);
        long endTime = System.currentTimeMillis();
        statsDClient.recordExecutionTime("db.ops.endpoint.bill.http.post", endTime - startTime);

        long apiEndTime = System.currentTimeMillis();
        statsDClient.recordExecutionTime("api.ops.endpoint.bill.http.post", apiEndTime - apiStartTime);

        return savedBill;
    }

    @GetMapping("/v2/bills")
    public @ResponseBody List<Bill> getAllBillsV2() {
        User currentUser = getCurrentUser();
        List<Bill> bills = billService.findAll(currentUser.getId());
        return bills;
    }

    @GetMapping("/v1/bills")
    public @ResponseBody List<Bill> getAllBills() {

        long apiStartTime = System.currentTimeMillis();
        statsDClient.incrementCounter("endpoint.bills.http.get");
        logger.info("Retrieving all bills");

        User currentUser = getCurrentUser();

        long startTime = System.currentTimeMillis();
        List<Bill> bills = billService.findAll(currentUser.getId());
        long endTime = System.currentTimeMillis();
        statsDClient.recordExecutionTime("db.ops.endpoint.bills.http.get", endTime - startTime);

        long apiEndTime = System.currentTimeMillis();
        statsDClient.recordExecutionTime("api.ops.endpoint.bills.http.get", apiEndTime - apiStartTime);

        return bills;
    }

    @GetMapping("/v1/bill/{id}")
    private @ResponseBody Bill getBill(@PathVariable String id) {

        long apiStartTime = System.currentTimeMillis();
        statsDClient.incrementCounter("endpoint.bill.http.get");
        logger.info("Retrieving bill... ID: " + id);

        User currentUser = getCurrentUser();

        long startTime = System.currentTimeMillis();
        Bill theBill = billService.findById(id);
        long endTime = System.currentTimeMillis();
        statsDClient.recordExecutionTime("db.ops.endpoint.bill.http.get", endTime - startTime);

        if (theBill == null || ! theBill.getOwnerId().equals(currentUser.getId())) {
            logger.error("Bill: " + id + " not found");
            throw new BillNotFoundException("Bill Not Found!");
        }
        long apiEndTime = System.currentTimeMillis();
        statsDClient.recordExecutionTime("api.ops.endpoint.bill.http.get", apiEndTime - apiStartTime);

        return theBill;
    }

    @DeleteMapping("/v1/bill/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private void deleteBill(@PathVariable String id) {

        long apiStartTime = System.currentTimeMillis();
        statsDClient.incrementCounter("endpoint.bill.http.delete");
        logger.info("Deleting bill... ID: " + id);

        User currentUser = getCurrentUser();
        Bill theBill = billService.findById(id);
        // throw exception when:
        // 1. passed id not exist;
        // 2. ownerId field in bill object is null;
        // 3. bill's ownerId is not equal to currentUser's id
        if (theBill == null || theBill.getOwnerId() == null ||
                ! theBill.getOwnerId().equals(currentUser.getId())) {
            logger.error("Bill: " + id + " not found");
            throw new BillNotFoundException("Bill Not Found!");
        }
        long startTime = System.currentTimeMillis();
        billService.deleteById(id);
        long endTime = System.currentTimeMillis();
        statsDClient.recordExecutionTime("db.ops.endpoint.bill.http.delete", endTime - startTime);

        long apiEndTime = System.currentTimeMillis();
        statsDClient.recordExecutionTime("api.ops.endpoint.bill.http.delete", apiEndTime - apiStartTime);
    }

    @PutMapping("/v1/bill/{id}")
    private @ResponseBody Bill updateBill(@RequestBody Bill bill, @PathVariable String id) {

        long apiStartTime = System.currentTimeMillis();
        statsDClient.incrementCounter("endpoint.bill.http.put");
        logger.info("Updating bill... ID: " + id);

        User currentUser = getCurrentUser();
        Bill theBill = billService.findById(id);
        if (theBill == null || ! currentUser.getId().equals(theBill.getOwnerId())) {
            logger.error("Bill: " + id + " not found");
            throw new BillNotFoundException("Bill Not Found!");
        }
        // pass those four read-only fields
        bill.setId(id);
        bill.setCreatedTs(theBill.getCreatedTs());
        bill.setUpdatedTs(dateFormat.format(new Date()));
        bill.setOwnerId(theBill.getOwnerId());

        long startTime = System.currentTimeMillis();
        billService.save(bill);
        long endTime = System.currentTimeMillis();
        statsDClient.recordExecutionTime("db.ops.endpoint.bill.http.put", endTime - startTime);

        long apiEndTime = System.currentTimeMillis();
        statsDClient.recordExecutionTime("api.ops.endpoint.bill.http.put", apiEndTime - apiStartTime);
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
