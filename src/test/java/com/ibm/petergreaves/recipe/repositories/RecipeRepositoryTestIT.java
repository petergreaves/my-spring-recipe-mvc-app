package com.ibm.petergreaves.recipe.repositories;

import com.ibm.petergreaves.recipe.commands.RecipeCommand;
import com.ibm.petergreaves.recipe.domain.Ingredient;
import com.ibm.petergreaves.recipe.domain.Recipe;
import com.ibm.petergreaves.recipe.domain.UnitOfMeasure;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
//@SpringBootTest
class RecipeRepositoryTestIT {

    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    UnitOfMeasureRepository unitOfMeasureRepository;

    @Test
    @Disabled
    void testIngredientsAreSet(){

        UnitOfMeasure cupUom = unitOfMeasureRepository.findByDescription("Cup").get();

        unitOfMeasureRepository.save(cupUom);

        Set<Ingredient> ingredients = new HashSet<>();
        Ingredient tomatoSauce = Ingredient.builder()
                .description("Tomato Sauce")
                .quantity(new BigDecimal(1))
                .uom(cupUom)
                .build();

        ingredients.add(tomatoSauce);
        Recipe recipe = Recipe.builder().ingredients(ingredients).id(33L).build();
        ingredients.forEach(ing ->recipe.addIngredient(ing));
        recipeRepository.save(recipe);
        Set<Ingredient> fromRepository = recipeRepository.findById(33L).get().getIngredients();

        assertEquals(ingredients.size(), fromRepository.size());
    }



}
