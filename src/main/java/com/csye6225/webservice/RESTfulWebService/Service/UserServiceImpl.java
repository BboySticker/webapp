package com.csye6225.webservice.RESTfulWebService.Service;

import com.csye6225.webservice.RESTfulWebService.Dao.UserDao;
import com.csye6225.webservice.RESTfulWebService.Entity.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Service
public class UserServiceImpl implements UserService {

	// need to inject user dao
	@Autowired
	private UserDao userDao;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	@Transactional
	public List<User> findAll() {
		return userDao.findAll();
	}

	@Override
	@Transactional
	public User findByUsername(String username) {
		// check the database if the user already exists
		return userDao.findByUsername(username);
	}

	@Override
	@Transactional
	public User save(User user) {
		User savedUser = new User();

		savedUser.setId(user.getId());
		savedUser.setPassword(passwordEncoder.encode(user.getPassword()));
		savedUser.setFirst_name(user.getFirst_name());
		savedUser.setLast_name(user.getLast_name());
		savedUser.setEmail_address(user.getEmail_address());

		DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");
		if (user.getAccount_created() == null) {
			savedUser.setAccount_created(dateFormat.format(new Date()));
		} else {
			savedUser.setAccount_created(user.getAccount_created());
		}
		savedUser.setAccount_updated(dateFormat.format(new Date()));

		// save user in the database
		userDao.save(savedUser);
		return savedUser;
	}

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		User user = userDao.findByUsername(userName);
		if (user == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		return new org.springframework.security.core.userdetails.User(user.getEmail_address(), user.getPassword(), AuthorityUtils.createAuthorityList("USER"));
	}

}
