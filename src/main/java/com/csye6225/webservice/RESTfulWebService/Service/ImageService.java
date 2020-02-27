package com.csye6225.webservice.RESTfulWebService.Service;

import com.csye6225.webservice.RESTfulWebService.Entity.Recipe.Image;
import com.csye6225.webservice.RESTfulWebService.Entity.Recipe.Recipe;

public interface ImageService {

    Image attach(Recipe recipe, Image image);

}
