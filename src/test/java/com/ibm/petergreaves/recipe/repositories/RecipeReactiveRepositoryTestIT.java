package com.ibm.petergreaves.recipe.repositories;

import com.ibm.petergreaves.recipe.domain.Recipe;
import com.ibm.petergreaves.recipe.repositories.reactive.RecipeReactiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataMongoTest
public class RecipeReactiveRepositoryTestIT {

    @Autowired
    RecipeReactiveRepository recipeReactiveRepository;

    @BeforeEach
    public void clear(){

        recipeReactiveRepository.deleteAll().block();
    }

    @Test
    public void getRecipe(){

        long k = recipeReactiveRepository.count().block();

        Recipe newRecipe = Recipe.builder().description("new recipe").build();

        Recipe savedNewRecipe = recipeReactiveRepository.save(newRecipe).block();
        long newK = recipeReactiveRepository.count().block();

        assertAll(
                () -> assertEquals(k, newK-1),
                () -> assertNotNull(savedNewRecipe)
        );
    }
}
