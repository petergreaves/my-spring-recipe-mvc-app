package com.ibm.petergreaves.recipe.utils;

import com.ibm.petergreaves.recipe.domain.Ingredient;
import com.ibm.petergreaves.recipe.domain.Recipe;

import java.util.HashSet;
import java.util.Set;

public class IngredientsBuilder {

    private static IngredientsBuilder builder;
    private final Set<Ingredient> ingredients;

    public IngredientsBuilder(){


        builder = new IngredientsBuilder();
        ingredients = new HashSet<>();

    }

    public IngredientsBuilder withIngredient(Ingredient i){

        ingredients.add(i);
        return builder;
    }

    public Set<Ingredient> build(){

        return ingredients;
    }
}
