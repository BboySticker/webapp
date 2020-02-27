package com.csye6225.webservice.RESTfulWebService.Service;

import com.csye6225.webservice.RESTfulWebService.Dao.RecipeDao;
import com.csye6225.webservice.RESTfulWebService.Entity.Recipe.NutritionInformation;
import com.csye6225.webservice.RESTfulWebService.Entity.Recipe.OrderedList;
import com.csye6225.webservice.RESTfulWebService.Entity.Recipe.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RecipeServiceImpl implements RecipeService {

    @Autowired
    private RecipeDao recipeDao;

    @Override
    @Transactional
    public Recipe save(Recipe recipe) {
        return recipeDao.save(recipe);
    }

    @Override
    @Transactional
    public void save(NutritionInformation nutritionInformation) {
        recipeDao.save(nutritionInformation);
    }

    @Override
    @Transactional
    public void save(OrderedList orderedList) {
        recipeDao.save(orderedList);
    }

    @Override
    @Transactional
    public Recipe getRecent() {
        return recipeDao.getRecent();
    }

    @Override
    @Transactional
    public Recipe findById(String id) {
        return recipeDao.findById(id);
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        recipeDao.deleteById(id);
    }
}
