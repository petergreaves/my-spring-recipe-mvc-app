package com.ibm.petergreaves.recipe.services;

import com.ibm.petergreaves.recipe.commands.RecipeCommand;
import com.ibm.petergreaves.recipe.domain.Recipe;

import java.util.List;
import java.util.Set;

public interface RecipeService {

    Set<Recipe> getRecipes();
    Recipe getRecipeByID(Long id);
    RecipeCommand saveRecipeCommand(RecipeCommand command);
    RecipeCommand findRecipeCommandByID(Long id);
}
