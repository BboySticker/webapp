package com.csye6225.webservice.RESTfulWebService.Dao;

import com.csye6225.webservice.RESTfulWebService.Entity.Recipe.Image;
import com.csye6225.webservice.RESTfulWebService.Entity.Recipe.Recipe;

public interface ImageDao {

    Image attach(Recipe recipe, Image image);

}
