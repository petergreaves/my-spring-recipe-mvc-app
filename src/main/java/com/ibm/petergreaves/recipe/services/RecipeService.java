package com.ibm.petergreaves.recipe.services;

import com.ibm.petergreaves.recipe.commands.RecipeCommand;
import com.ibm.petergreaves.recipe.domain.Recipe;
import com.ibm.petergreaves.recipe.exceptions.NotFoundException;


import java.util.Set;

public interface RecipeService {

    Set<Recipe> getRecipes();
    Recipe getRecipeByID(Long id) throws NotFoundException;
    RecipeCommand saveRecipeCommand(RecipeCommand command);
    RecipeCommand findRecipeCommandByID(Long id);
    void deleteByID(Long id);
}
