package com.ibm.petergreaves.recipe.services;

import com.ibm.petergreaves.recipe.commands.CategoryCommand;
import com.ibm.petergreaves.recipe.commands.IngredientCommand;
import com.ibm.petergreaves.recipe.commands.RecipeCommand;
import com.ibm.petergreaves.recipe.converters.*;
import com.ibm.petergreaves.recipe.domain.Category;
import com.ibm.petergreaves.recipe.domain.Ingredient;
import com.ibm.petergreaves.recipe.domain.Recipe;
import com.ibm.petergreaves.recipe.repositories.RecipeRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class RecipeServiceImpl implements RecipeService{


    private final RecipeRepository recipeRepository;

    public RecipeServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public Set<Recipe> getRecipes() {
        log.debug("Getting recipes");
        Set<Recipe> recipes = new HashSet<>();
        recipeRepository.findAll().iterator().forEachRemaining(recipes::add);

        return recipes;
    }

    @Override
    public Recipe getRecipeByID(Long id) {
        log.debug("Getting recipe with ID " +id);

        Optional<Recipe> recipeOptional = recipeRepository.findById(id);

        if (recipeOptional.isEmpty()){

            throw new RuntimeException("No recipe found with id : " +id);
        }
        else return recipeOptional.get();
        }


    @Override
    @Transactional
    public RecipeCommand saveRecipeCommand(RecipeCommand command) {

        Recipe detachedRecipe = new RecipeCommandToRecipe().convert(command);
        Recipe savedRecipe = recipeRepository.save(detachedRecipe);
        return new RecipeToRecipeCommand().convert(savedRecipe);
    }
}
