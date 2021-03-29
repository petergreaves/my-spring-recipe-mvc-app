package com.ibm.petergreaves.recipe.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RecipeTest {

    @Test
    void ingredientsAreSetByBuilder(){

    UnitOfMeasure cupUom = new UnitOfMeasure();
    cupUom.setId("12");
    cupUom.setDescription("Cup");

        Set<Ingredient> ingredients = new HashSet<>();
        Ingredient tomatoSauce = new Ingredient();
        tomatoSauce.setDescription("Tomato Sauce");
        tomatoSauce.setQuantity(new BigDecimal(1));
        tomatoSauce.setUom(cupUom);


        ingredients.add(tomatoSauce);
        Recipe recipe = Recipe.builder().ingredients(ingredients).build();
        Set<Ingredient> fromRecipe = recipe.getIngredients();

        assertEquals(ingredients.size(), fromRecipe.size());
    }



}
