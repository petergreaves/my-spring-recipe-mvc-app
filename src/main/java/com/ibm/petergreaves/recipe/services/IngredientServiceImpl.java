package com.ibm.petergreaves.recipe.services;

import com.ibm.petergreaves.recipe.commands.IngredientCommand;
import com.ibm.petergreaves.recipe.converters.IngredientCommandToIngredient;
import com.ibm.petergreaves.recipe.converters.IngredientToIngredientCommand;
import com.ibm.petergreaves.recipe.domain.Ingredient;
import com.ibm.petergreaves.recipe.domain.Recipe;
import com.ibm.petergreaves.recipe.repositories.reactive.RecipeReactiveRepository;
import com.ibm.petergreaves.recipe.repositories.reactive.UnitOfMeasureReactiveRepository;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;



import java.util.Optional;
import java.util.Set;


@Service
@Slf4j
public class IngredientServiceImpl implements IngredientService {

    final private RecipeReactiveRepository recipeReactiveRepository;
    final private IngredientCommandToIngredient ingredientCommandToIngredient;
    final private IngredientToIngredientCommand ingredientToIngredientCommand;
    final private UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;


    public IngredientServiceImpl(RecipeReactiveRepository recipeReactiveRepository,
                                 IngredientCommandToIngredient ingredientCommandToIngredient,
                                 IngredientToIngredientCommand ingredientToIngredientCommand,
                                 UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository) {
        this.recipeReactiveRepository = recipeReactiveRepository;
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.unitOfMeasureReactiveRepository = unitOfMeasureReactiveRepository;
    }

    @Override
    public IngredientCommand findByRecipeIdAndIngredientId(String recipeId, String ingredientId) {

        Optional<Recipe> recipeOptional = recipeReactiveRepository.findById(recipeId).blockOptional();

        if (recipeOptional.isEmpty()) {

            log.error("No such recipe with id " + recipeId);
            //todo error handling
        }

        Set<Ingredient> ingredients = recipeOptional.get().getIngredients();

        Optional<IngredientCommand> retval = ingredients.stream()
                .filter(in -> in.getId().equals(ingredientId))
                .map(ingredientToIngredientCommand::convert)
                .findFirst();
        if (!retval.isPresent()) {

            log.error("No such ingredient with id " + ingredientId);
            //todo error handling
        }
        else{

            retval.get().setRecipeID(recipeId);
        }
        return retval.get();
    }

    @Override
    public IngredientCommand saveIngredientCommand(IngredientCommand command) {

        // what recipe?
        Optional<Recipe> recipeOptional = recipeReactiveRepository.findById(command.getRecipeID()).blockOptional();

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
                found.setUom(unitOfMeasureReactiveRepository
                        .findById(command.getUom().getId()).block());


            } else {
                // its a new ingredient
                log.debug("This is a new ingredient for the recipe");
                Ingredient newIngredient = ingredientCommandToIngredient.convert(command);
                recipe.addIngredient(newIngredient);

            }

            // either way, save it
            Recipe savedRecipe = recipeReactiveRepository.save(recipe).block();


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

            IngredientCommand ingredientCommand = ingredientToIngredientCommand.convert(savedIngredientOptional.get());
            ingredientCommand.setRecipeID(recipe.getId());
            return ingredientCommand;
        }

    }

    @Override

    public void removeIngredientCommand(IngredientCommand command) {

        String ingredientID = command.getId();

        // find the recipe to which this Ingred belongs

        Recipe recipe;

        Optional<Recipe> recipeOptional = recipeReactiveRepository.findById(command.getRecipeID()).blockOptional();

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
            recipeReactiveRepository.save(recipe).block();
        }


    }
}
