package com.csye6225.webservice.RESTfulWebService.Service;

import com.csye6225.webservice.RESTfulWebService.Dao.ImageDao;
import com.csye6225.webservice.RESTfulWebService.Dao.RecipeDao;
import com.csye6225.webservice.RESTfulWebService.Entity.Recipe.Image;
import com.csye6225.webservice.RESTfulWebService.Entity.Recipe.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageDao imageDao;

    @Autowired
    private RecipeDao recipeDao;

    @Override
    public Image attach(Recipe recipe, Image image) {
        // update the recipe table in database
        recipe.setImage(image);
        recipeDao.save(recipe);
        return imageDao.attach(recipe, image);
    }

}
