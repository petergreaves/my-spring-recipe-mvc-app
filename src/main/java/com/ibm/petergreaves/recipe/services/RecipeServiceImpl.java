package com.ibm.petergreaves.recipe.services;

import com.ibm.petergreaves.recipe.commands.RecipeCommand;
import com.ibm.petergreaves.recipe.converters.*;
import com.ibm.petergreaves.recipe.domain.Recipe;
import com.ibm.petergreaves.recipe.exceptions.NotFoundException;

import com.ibm.petergreaves.recipe.repositories.reactive.RecipeReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Slf4j
@Service
public class RecipeServiceImpl implements RecipeService{


    private final RecipeReactiveRepository recipeRepository;
    private final RecipeToRecipeCommand recipeToRecipeCommand;
    private final RecipeCommandToRecipe recipeCommandToRecipe;

    public RecipeServiceImpl(RecipeReactiveRepository recipeRepository, RecipeToRecipeCommand recipeToRecipeCommand, RecipeCommandToRecipe recipeCommandToRecipe) {
        this.recipeRepository = recipeRepository;
        this.recipeToRecipeCommand = recipeToRecipeCommand;
        this.recipeCommandToRecipe = recipeCommandToRecipe;
    }

    @Override
    public Flux<Recipe> getRecipes() {
        log.debug("Getting recipes");

        return recipeRepository.findAll();
    }

    @Override
    public Mono<RecipeCommand> findRecipeCommandByID(String  id) {

        return recipeRepository.findById(id).map( recipe -> {

                RecipeCommand command = recipeToRecipeCommand.convert(recipe);

                command.getIngredients().forEach(ing ->ing.setRecipeID(recipe.getId()));
                return command;
         }

        );

    }

    @Override
    public Mono<Recipe> getRecipeByID(String id) throws NotFoundException {
        log.debug("Getting recipe with ID " +id);


        return recipeRepository.findById(id);

        }


    @Override
    public Mono<RecipeCommand> saveRecipeCommand(RecipeCommand command) {

        log.info("Saving recipe command with id : "+command.getId());
        Recipe detachedRecipe = new RecipeCommandToRecipe().convert(command);
        return recipeRepository.save(detachedRecipe).map(recipeToRecipeCommand::convert);

    }

    @Override
    public Mono<Void> deleteByID(String id) {

        recipeRepository.deleteById(id).block();
        return Mono.empty();
    }


}
