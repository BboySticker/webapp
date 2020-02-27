package com.csye6225.webservice.RESTfulWebService.Dao;

import com.csye6225.webservice.RESTfulWebService.Entity.Recipe.Image;
import com.csye6225.webservice.RESTfulWebService.Entity.Recipe.Recipe;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ImageDaoImpl implements ImageDao {

    // inject the session factory
    private SessionFactory sessionFactory;

    @Autowired
    public ImageDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Image attach(Recipe recipe, Image image) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.saveOrUpdate(image);
        return image;
    }

}
