package com.ibm.petergreaves.recipe.services;

import com.ibm.petergreaves.recipe.commands.IngredientCommand;
import com.ibm.petergreaves.recipe.converters.IngredientCommandToIngredient;
import com.ibm.petergreaves.recipe.converters.IngredientToIngredientCommand;
import com.ibm.petergreaves.recipe.domain.Ingredient;
import com.ibm.petergreaves.recipe.domain.Recipe;
import com.ibm.petergreaves.recipe.repositories.RecipeRepository;
import com.ibm.petergreaves.recipe.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;


import javax.persistence.criteria.CriteriaBuilder;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@Service
@Slf4j
public class IngredientServiceImpl implements IngredientService {

    final private RecipeRepository recipeRepository;
    final private IngredientCommandToIngredient ingredientCommandToIngredient;
    final private IngredientToIngredientCommand ingredientToIngredientCommand;
    final private UnitOfMeasureRepository unitOfMeasureRepository;

    public IngredientServiceImpl(RecipeRepository recipeRepository, IngredientCommandToIngredient ingredientCommandToIngredient, IngredientToIngredientCommand ingredientToIngredientCommand, UnitOfMeasureRepository unitOfMeasureRepository) {
        this.recipeRepository = recipeRepository;
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
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
        if (!retval.isPresent()) {

            log.error("No such ingredient with id " + ingredientId);
            //todo error handling
        }

        return retval.get();
    }

    @Override
    public IngredientCommand saveIngredientCommand(IngredientCommand command) {

        // what recipe?
        Optional<Recipe> recipeOptional = recipeRepository.findById(command.getRecipeID());

        if (!recipeOptional.isPresent()) {
            // we are updating an ingredient to an existing recipe

            log.error("Recipe not found for id : " + command.getRecipeID());
            return new IngredientCommand();
        } else {
            Recipe recipe = recipeOptional.get();

            // find the ingredients
            Set<Ingredient> ingredients = recipe.getIngredients();
            // find this ingred by ID if we can

            Optional<Ingredient> ingredientOptional = ingredients
                    .stream()
                    .filter(in -> in.getId().equals(command.getId()))
                    .findFirst();

            if (ingredientOptional.isPresent()) {   // we can so update it
                Ingredient found = ingredientOptional.get();
                found.setDescription(command.getDescription());
                found.setQuantity(command.getQuantity());
                found.setUom(unitOfMeasureRepository
                        .findById(command.getUom().getId())
                        .orElseThrow(() -> new RuntimeException("Unit of measure NOT FOUND"))); //todo address this


            } else {
                // its a new ingredient
                log.debug("This is a new ingredient for the recipe");
                Ingredient newIngredient = ingredientCommandToIngredient.convert(command);
                newIngredient.setRecipe(recipe);
                recipe.addIngredient(newIngredient);

            }

            // either way, save it
            Recipe savedRecipe = recipeRepository.save(recipe);


            // was this an ingredient that had no ID, e.g. completely new?
            Optional<Ingredient> savedIngredientOptional = savedRecipe.getIngredients()
                    .stream()
                    .filter(ings -> ings.getId().equals(command.getId()))
                    .findFirst();

            if (!savedIngredientOptional.isPresent()) {

                savedIngredientOptional = savedRecipe.getIngredients()
                        .stream()
                        .filter(ings -> ings.getDescription().equals(command.getDescription()))
                        .filter(ings -> ings.getQuantity().equals(command.getQuantity()))
                        .filter(ings -> ings.getUom().getId().equals(command.getUom().getId()))
                        .findFirst();

            }
            // to do check for fail
            return ingredientToIngredientCommand.convert(savedIngredientOptional.get());
        }

    }

    @Override

    public void removeIngredientCommand(IngredientCommand command) {

        Long recipeID = command.getRecipeID();
        ;
        Long ingredientID = command.getId();

        // find the recipe to which this Ingred belongs

        Recipe recipe;

        Optional<Recipe> recipeOptional = recipeRepository.findById(command.getRecipeID());

        //bail early
        // TODO throw something, better than return

        if (!recipeOptional.isPresent()) {
            log.error("No such recipe with id : " + command.getRecipeID());
            return;
        } else {
            recipe = recipeOptional.get();
        }

        Optional<Ingredient> toDelete= recipe.getIngredients()
                .stream()
                .filter(in -> in.getId().equals(ingredientID))
                .findFirst();


        if (toDelete.isPresent()){
            log.debug("found Ingredient");
            Ingredient ingredientToDelete = toDelete.get();
            ingredientToDelete.setRecipe(null);
            recipe.getIngredients().remove(toDelete.get());
            recipeRepository.save(recipe);
        }


    }
}
