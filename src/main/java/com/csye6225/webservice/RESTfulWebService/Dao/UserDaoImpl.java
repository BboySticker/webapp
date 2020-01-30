package com.csye6225.webservice.RESTfulWebService.Dao;

import com.csye6225.webservice.RESTfulWebService.Entity.User;
import com.csye6225.webservice.RESTfulWebService.Exception.UserNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {

	// need to inject the session factory
	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Override
	public List<User> findAll() {
		Session currentSession = sessionFactory.getCurrentSession();

		// now retrieve/read from database using username
		Query<User> theQuery =
				currentSession.createQuery("from User", User.class);

		List<User> users = theQuery.getResultList();

		return users;
	}

	@Override
	public User findByUsername(String theUsername) {
		// get the current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();

		Query<User> theQuery = currentSession.createQuery("from User where email_address=:uEmail", User.class);
		theQuery.setParameter("uEmail", theUsername);
		User theUser;
		try {
			theUser = theQuery.getSingleResult();
		} catch (Exception e) {
			theUser = null;
		}
		return theUser;
	}

	@Override
	public void save(User theUser) {
		// get current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		// create the user
		currentSession.saveOrUpdate(theUser);
	}

	@Override
	public User login(String username, String password) {

		User user = findByUsername(username);

		if (user == null) {
			throw new UserNotFoundException("User not found");
		}

		if (passwordEncoder.matches(password, user.getPassword())) {
			return user;
		} else {
			return null;
		}
	}

	@Override
	public Optional<User> findByToken(String token) {

		Session currentSession = sessionFactory.getCurrentSession();

		Query<User> theQuery =
				currentSession.createQuery("from User where token=:uToken", User.class);

		theQuery.setParameter("uToken", token);

		User theUser;
		try {
			theUser = theQuery.getSingleResult();
			return Optional.of(theUser);
		} catch (Exception e) {
			return Optional.empty();
		}
	}

}
