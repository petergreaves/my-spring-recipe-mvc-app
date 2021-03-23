package com.ibm.petergreaves.recipe.repositories.reactive;

import com.ibm.petergreaves.recipe.domain.Recipe;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface RecipeReactiveRepository extends ReactiveMongoRepository<Recipe, String> {

    Flux<Recipe> findAll() ;
}
