package com.csye6225.webservice.RESTfulWebService.Service;

import com.csye6225.webservice.RESTfulWebService.Dao.BillDao;
import com.csye6225.webservice.RESTfulWebService.Entity.Bill;
import com.csye6225.webservice.RESTfulWebService.Entity.User;
import com.csye6225.webservice.RESTfulWebService.Exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class BillServiceImpl implements BillService {

    @Autowired
    private BillDao billDao;

    @Autowired
    private UserService userService;

    private Logger logger = Logger.getLogger(getClass().getName());

    @Override
    @Transactional
    public Bill findById(String id) {
        return billDao.findById(id);
    }

    @Override
    @Transactional
    public List<Bill> findAll() {
        return billDao.findAll();
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        billDao.deleteById(id);
    }

    @Override
    @Transactional
    public Bill save(Bill bill) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String username = auth.getName();

        logger.info("Successfully obtained user: " + username);

        User user = userService.findByUsername(username);

        if (user == null) {
            throw new UserNotFoundException("User Not Found!");
        }

        // set those read-only attributes: id, createdTs, updatedTs, ownerId
        bill.setId(UUID.randomUUID().toString());
        bill.setCreatedTs(new Date());
        bill.setUpdatedTs(new Date());
        bill.setOwnerId(user.getId());

        billDao.save(bill);

        return bill;
    }
}
