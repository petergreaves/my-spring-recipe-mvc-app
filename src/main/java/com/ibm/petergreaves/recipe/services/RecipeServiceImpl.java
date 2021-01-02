package com.ibm.petergreaves.recipe.services;

import com.ibm.petergreaves.recipe.domain.*;
import com.ibm.petergreaves.recipe.repositories.CategoryRepository;
import com.ibm.petergreaves.recipe.repositories.RecipeRepository;
import com.ibm.petergreaves.recipe.repositories.UnitOfMeasureRepository;
import com.ibm.petergreaves.recipe.utils.IngredientsBuilder;
import com.ibm.petergreaves.recipe.utils.RecipeBuilder;
import net.bytebuddy.description.type.TypeDescription;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class RecipeServiceImpl implements RecipeService{


    private final RecipeRepository recipeRepository;

    public RecipeServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public Set<Recipe> getRecipes() {

        Set<Recipe> recipes = new TreeSet<>();
        recipeRepository.findAll().iterator().forEachRemaining(recipes::add);

        return recipes;


    }
}
