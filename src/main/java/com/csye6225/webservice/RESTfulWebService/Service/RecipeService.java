package com.csye6225.webservice.RESTfulWebService.Service;

import com.csye6225.webservice.RESTfulWebService.Entity.Recipe.NutritionInformation;
import com.csye6225.webservice.RESTfulWebService.Entity.Recipe.OrderedList;
import com.csye6225.webservice.RESTfulWebService.Entity.Recipe.Recipe;

public interface RecipeService {

    Recipe save(Recipe recipe);

    void save(NutritionInformation nutritionInformation);

    void save(OrderedList orderedList);

    Recipe getRecent();

    Recipe findById(String id);

    void deleteById(String id);

}
