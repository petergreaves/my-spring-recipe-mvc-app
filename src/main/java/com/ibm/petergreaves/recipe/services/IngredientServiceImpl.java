package com.ibm.petergreaves.recipe.services;

import com.ibm.petergreaves.recipe.commands.IngredientCommand;
import com.ibm.petergreaves.recipe.commands.RecipeCommand;
import com.ibm.petergreaves.recipe.converters.IngredientCommandToIngredient;
import com.ibm.petergreaves.recipe.converters.IngredientToIngredientCommand;
import com.ibm.petergreaves.recipe.converters.UnitOfMeasureCommandToUnitOfMeasure;
import com.ibm.petergreaves.recipe.converters.UnitOfMeasureToUnitOfMeasureCommand;
import com.ibm.petergreaves.recipe.domain.Ingredient;
import com.ibm.petergreaves.recipe.domain.Recipe;
import com.ibm.petergreaves.recipe.domain.UnitOfMeasure;
import com.ibm.petergreaves.recipe.repositories.reactive.RecipeReactiveRepository;
import com.ibm.petergreaves.recipe.repositories.reactive.UnitOfMeasureReactiveRepository;
import com.mongodb.internal.connection.tlschannel.ServerTlsChannel;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;


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
    public Mono<IngredientCommand> findByRecipeIdAndIngredientId(String recipeId, String ingredientId) {

        return recipeReactiveRepository
                .findById(recipeId)
                .flatMapIterable(Recipe::getIngredients)
                .filter(ingredient -> ingredient.getId().equalsIgnoreCase(ingredientId))
                .single()
                .map(ingredient -> {
                    IngredientCommand command = ingredientToIngredientCommand.convert(ingredient);
                    command.setRecipeID(recipeId);
                    return command;
                });

    }


    /**
     * this method adds a ingrrdient to a recipe
     * <p>
     * first check the recipe exists
     * <p>
     * if it does, then 1. check if the ingredient already exists - if it does
     * just update the quantity, uom(?) and description.  if it doesn it is a new one ,
     * so add it to the ingrediuents collection.  then save the recipe, retrievve and return
     * the updated ingredient
     *
     * @param command
     * @return
     */
    @Override
    public Mono<IngredientCommand> saveIngredientCommand(IngredientCommand command) {

        Mono<IngredientCommand> ic = recipeReactiveRepository.findById(command.getRecipeID())
                .log()
                .map(rec -> rec.getIngredients()
                        .stream()
                        .filter(ingredient -> ingredient.getId().equals(command.getId()))
                        .findFirst()
                        .map(ing -> {
                            ing.setDescription(command.getDescription());
                            ing.setQuantity(command.getQuantity());
                            recipeReactiveRepository.save(rec)
                                    .subscribe(updated -> log.info("Recipe updated for existing ingredient : {}", ing.getDescription()));

                            return ingredientToIngredientCommand.convert(ing);
                        })
                        .orElseGet(() -> {  //must be a new ingred
                            Ingredient newOne = new Ingredient();
                            newOne.setDescription(command.getDescription());
                            newOne.setQuantity(command.getQuantity());
                            newOne.setId(UUID.randomUUID().toString());
                            // get the OUM
                            Mono<UnitOfMeasure> uomMono = unitOfMeasureReactiveRepository.findById(command.getUom().getId());
                            uomMono.subscribe(u -> {
                                log.info("UOM ID : " + u.getId());
                                newOne.setUom(u);
                                rec.addIngredient(newOne);
                                recipeReactiveRepository.save(rec)
                                        .subscribe(updated -> log.info("Recipe updated for new ingredient : {} ", newOne.getDescription()));
                            });
                            return ingredientToIngredientCommand.convert(newOne);
                        })
                );


        ic.subscribe();
        return ic;
    }

    @Override
    public Mono<Void> removeIngredientCommand(IngredientCommand command) {

        recipeReactiveRepository.findById(command.getRecipeID()).map(rec -> {
            rec.removeIngredient(ingredientCommandToIngredient.convert(command));
            recipeReactiveRepository.save(rec).subscribe();
            return Mono.empty();
        }).subscribe(updated -> log.info("Ingredient {} removed from recipe : {}", command.getDescription(), command.getRecipeID()));


        return Mono.empty();
    }
}
