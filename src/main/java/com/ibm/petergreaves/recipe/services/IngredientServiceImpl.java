package com.ibm.petergreaves.recipe.services;

import com.ibm.petergreaves.recipe.commands.IngredientCommand;
import com.ibm.petergreaves.recipe.converters.IngredientToIngredientCommand;
import com.ibm.petergreaves.recipe.domain.Ingredient;
import com.ibm.petergreaves.recipe.domain.Recipe;
import com.ibm.petergreaves.recipe.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;


@Service
@Slf4j
public class IngredientServiceImpl implements IngredientService {

    final private RecipeRepository recipeRepository;
    final private IngredientToIngredientCommand ingredientToIngredientCommand;

    public IngredientServiceImpl(RecipeRepository recipeRepository, IngredientToIngredientCommand ingredientToIngredientCommand) {
        this.recipeRepository = recipeRepository;
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
    }

    @Override
    public IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId) {

        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);

        if (recipeOptional.isEmpty()) {

            log.error("No such recipe with id " + recipeId);
            //todo error handling
        }

        Set<Ingredient> ingredients = recipeOptional.get().getIngredients();

        Optional<IngredientCommand> retval = ingredients.stream()
                .filter(in -> in.getId().equals(ingredientId))
                .map(in -> ingredientToIngredientCommand.convert(in))
                .findFirst();
        if (!retval.isPresent()){

            log.error("No such ingredient with id " +  ingredientId);
            //todo error handling
        }

        return retval.get();
    }
}
