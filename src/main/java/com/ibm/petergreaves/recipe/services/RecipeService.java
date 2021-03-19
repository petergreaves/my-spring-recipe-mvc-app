package com.ibm.petergreaves.recipe.services;

import com.ibm.petergreaves.recipe.commands.RecipeCommand;
import com.ibm.petergreaves.recipe.domain.Recipe;
import com.ibm.petergreaves.recipe.exceptions.NotFoundException;


import java.util.Set;

public interface RecipeService {

    Set<Recipe> getRecipes();
    Recipe getRecipeByID(String id) throws NotFoundException;
    RecipeCommand saveRecipeCommand(RecipeCommand command);
    RecipeCommand findRecipeCommandByID(String id);
    void deleteByID(String id);
}
