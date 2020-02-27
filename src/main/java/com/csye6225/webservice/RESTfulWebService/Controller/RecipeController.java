package com.csye6225.webservice.RESTfulWebService.Controller;

import com.csye6225.webservice.RESTfulWebService.Entity.Recipe.NutritionInformation;
import com.csye6225.webservice.RESTfulWebService.Entity.Recipe.OrderedList;
import com.csye6225.webservice.RESTfulWebService.Entity.Recipe.Recipe;
import com.csye6225.webservice.RESTfulWebService.Entity.User.User;
import com.csye6225.webservice.RESTfulWebService.Exception.RecipeNotFoundException;
import com.csye6225.webservice.RESTfulWebService.Exception.UpdateRecipeFailedException;
import com.csye6225.webservice.RESTfulWebService.Exception.UserNotFoundException;
import com.csye6225.webservice.RESTfulWebService.Service.RecipeService;
import com.csye6225.webservice.RESTfulWebService.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@RestController
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private UserService userService;

    private Logger logger = Logger.getLogger(getClass().getName());

    private Recipe newestRecipe = null;

    @PostMapping("/v1/recipe")
    @ResponseStatus(HttpStatus.CREATED)
    private @ResponseBody Recipe createRecipe(@RequestBody Recipe recipe) {

        recipe.setId(UUID.randomUUID().toString());
        Date now = new Date();
        recipe.setCreatedTs(now.toString());
        recipe.setUpdatedTs(now.toString());
        recipe.setAuthorId(getCurrentUser().getId());

        Recipe theRecipe = recipeService.save(recipe);

        // update(cache) the newest recipe
        newestRecipe = theRecipe;

        NutritionInformation theNutritionInfo = recipe.getNutritionInformation();
        recipeService.save(theNutritionInfo);

        List<OrderedList> orderedLists = recipe.getSteps();
        for (OrderedList orderedList: orderedLists) {
            orderedList.setRecipe(theRecipe);
            recipeService.save(orderedList);
        }

        return theRecipe;
    }

    @GetMapping("/v1/recipes")
    private @ResponseBody Recipe getMostRecent() {

        if (newestRecipe != null) {
            logger.info("Retrieve from local cache: " + newestRecipe);
            return newestRecipe;
        }
        Recipe theRecipe = recipeService.getRecent();

        newestRecipe = theRecipe;

        if (theRecipe == null) {
           throw new RecipeNotFoundException("Recipe Not Found!");
        }
        return theRecipe;
    }

    @GetMapping("/v1/recipe/{id}")
    private @ResponseBody Recipe retrieveById(@PathVariable String id) {

        return recipeService.findById(id);

    }

    @DeleteMapping("/v1/recipe/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private void deleteById(@PathVariable String id) {

        if (newestRecipe != null && newestRecipe.getId().equals(id)) {
            newestRecipe = null;
        }
        recipeService.deleteById(id);

    }

    @PutMapping("/v1/recipe/{id}")
    private @ResponseBody Recipe updateRecipe(@PathVariable String id, @RequestBody Recipe recipe) {

        Recipe theRecipe = retrieveById(id);

        // Lazy solution to update... Delete the old one and save a new one...
        deleteById(id);

        if (theRecipe.getAuthorId() != null && ! theRecipe.getAuthorId().equals(getCurrentUser().getId())) {
            throw new UpdateRecipeFailedException("Update Recipe Failed!");
        }

        // Copy the old values from exist recipe, then update
        recipe.setId(theRecipe.getId());
        recipe.setCreatedTs(theRecipe.getCreatedTs());
        Date now = new Date();
        recipe.setUpdatedTs(now.toString());
        recipe.setAuthorId(theRecipe.getAuthorId());

        NutritionInformation theNutritionInfo = recipe.getNutritionInformation();
        recipeService.save(theNutritionInfo);

        List<OrderedList> orderedLists = recipe.getSteps();
        for (OrderedList orderedList: orderedLists) {
            orderedList.setRecipe(recipe);
            recipeService.save(orderedList);
        }

        return recipeService.save(recipe);

    }

    // helper function to get current authenticated user
    private User getCurrentUser() {

        // Get current authenticated user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.findByUsername(username);

        logger.info("Successfully obtained user: " + username);

        if (user == null) {
            throw new UserNotFoundException("User Not Found!");
        }

        return user;
    }

}
