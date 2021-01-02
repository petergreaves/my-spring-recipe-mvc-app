package com.ibm.petergreaves.recipe.services;

import com.ibm.petergreaves.recipe.domain.Recipe;

import java.util.List;
import java.util.Set;

public interface RecipeService {

    Set<Recipe> getRecipes();
}
