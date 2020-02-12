package com.csye6225.webservice.RESTfulWebService.Dao;

import com.csye6225.webservice.RESTfulWebService.Entity.Authorities;
import com.csye6225.webservice.RESTfulWebService.Entity.User;
import com.csye6225.webservice.RESTfulWebService.Entity.Users;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

	// need to inject the session factory
	@Autowired
	private SessionFactory sessionFactory;

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

		// save user to the table that usd to auth
		Users users = new Users();
		users.setUsername(theUser.getEmail_address());
		users.setPassword(theUser.getPassword());
		users.setEnabled(1);
		currentSession.saveOrUpdate(users);

		// save the authority
		Authorities authorities = new Authorities();
		authorities.setUsername(theUser.getEmail_address());
		authorities.setAuthority("ROLE_USER");
		currentSession.saveOrUpdate(authorities);

	}

}
