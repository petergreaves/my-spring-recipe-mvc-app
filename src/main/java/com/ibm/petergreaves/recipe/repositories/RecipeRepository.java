package com.ibm.petergreaves.recipe.repositories;

import com.ibm.petergreaves.recipe.domain.Recipe;
import org.springframework.data.repository.CrudRepository;

public interface RecipeRepository extends CrudRepository<Recipe, String> {

     Iterable<Recipe> findAll() ;
}
