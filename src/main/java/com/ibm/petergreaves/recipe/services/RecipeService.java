package com.ibm.petergreaves.recipe.services;

import com.ibm.petergreaves.recipe.commands.RecipeCommand;
import com.ibm.petergreaves.recipe.domain.Recipe;
import com.ibm.petergreaves.recipe.exceptions.NotFoundException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import java.util.Optional;
import java.util.Set;

public interface RecipeService {

    Flux<Recipe> getRecipes();
    Mono<Recipe> getRecipeByID(String id);
    Mono<RecipeCommand> saveRecipeCommand(RecipeCommand command);
    Mono<RecipeCommand> findRecipeCommandByID(String id);
    Mono<Void> deleteByID(String id);
}
