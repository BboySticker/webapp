package com.csye6225.webservice.RESTfulWebService.Service;

import com.csye6225.webservice.RESTfulWebService.Dao.UserDao;
import com.csye6225.webservice.RESTfulWebService.Entity.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class UserServiceImpl implements UserService {

	// need to inject user dao
	@Autowired
	private UserDao userDao;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

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

		// assign user details to the user object
		savedUser.setId(user.getId());

		savedUser.setPassword(passwordEncoder.encode(user.getPassword()));

		savedUser.setFirst_name(user.getFirst_name());
		savedUser.setLast_name(user.getLast_name());

		savedUser.setEmail_address(user.getEmail_address());

		if (user.getAccount_created() == null) {
			savedUser.setAccount_created(new Date());
		} else {
			savedUser.setAccount_created(user.getAccount_created());
		}

		savedUser.setAccount_updated(new Date());

		savedUser.setToken(user.getToken());

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

	@Override
	@Transactional
	public String login(String username, String password) {
		User user = userDao.login(username, password);
		if(user != null) {
			String token = UUID.randomUUID().toString();
			user.setToken(token);
			userDao.save(user);
			return token;
		}
		return StringUtils.EMPTY;
	}

	@Override
	@Transactional
	public Optional<org.springframework.security.core.userdetails.User> findByToken(String token) {
		Optional<User> user = userDao.findByToken(token);
		if(user.isPresent()){
			User user1 = user.get();
			org.springframework.security.core.userdetails.User newUser =
					new org.springframework.security.core.userdetails.User(user1.getEmail_address(), user1.getPassword(),
							true, true, true, true,
							AuthorityUtils.createAuthorityList("USER"));
			return Optional.of(newUser);
		}
		return  Optional.empty();
	}

}
