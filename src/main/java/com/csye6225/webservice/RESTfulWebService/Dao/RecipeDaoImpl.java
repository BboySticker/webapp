package com.csye6225.webservice.RESTfulWebService.Dao;

import com.csye6225.webservice.RESTfulWebService.Entity.Bill.Bill;
import com.csye6225.webservice.RESTfulWebService.Entity.Recipe.NutritionInformation;
import com.csye6225.webservice.RESTfulWebService.Entity.Recipe.OrderedList;
import com.csye6225.webservice.RESTfulWebService.Entity.Recipe.Recipe;
import com.csye6225.webservice.RESTfulWebService.Exception.RecipeNotFoundException;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RecipeDaoImpl implements RecipeDao {

    // inject the session factory
    private SessionFactory sessionFactory;

    @Autowired
    public RecipeDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Recipe save(Recipe recipe) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.saveOrUpdate(recipe);
        return recipe;
    }

    @Override
    public void save(NutritionInformation nutritionInformation) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.saveOrUpdate(nutritionInformation);
    }

    @Override
    public void save(OrderedList orderedList) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.saveOrUpdate(orderedList);
    }

    @Override
    public Recipe getRecent() {
        Session currentSession = sessionFactory.getCurrentSession();
        Query theQuery =
                currentSession.createQuery("from Recipe order by createdTs DESC");
        theQuery.setMaxResults(1);
        List resultList = theQuery.getResultList();

        if (resultList.size() == 0) {
            throw new RecipeNotFoundException("Recipe Not Found!");
        }
        Recipe theRecipe = (Recipe) resultList.get(0);
        System.out.println(theRecipe);

        return theRecipe;
    }

    @Override
    public Recipe findById(String id) {
        Session currentSession = sessionFactory.getCurrentSession();
        Recipe theRecipe = currentSession.get(Recipe.class, id);

        if (theRecipe == null) {
            throw new RecipeNotFoundException("Recipe Not Found!");
        }
        Hibernate.initialize(theRecipe.getSteps());
        return theRecipe;
    }

    @Override
    public void deleteById(String id) {
        Session currentSession = sessionFactory.getCurrentSession();
        Recipe theRecipe = findById(id);
        System.out.println(theRecipe);
        if (theRecipe.getSteps() == null) {
            System.out.println("Steps is null");
        }
        currentSession.delete(theRecipe);
    }
}
