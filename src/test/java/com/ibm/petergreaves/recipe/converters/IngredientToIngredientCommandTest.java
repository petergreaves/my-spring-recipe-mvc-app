package com.ibm.petergreaves.recipe.converters;

import com.ibm.petergreaves.recipe.commands.IngredientCommand;
import com.ibm.petergreaves.recipe.commands.NotesCommand;
import com.ibm.petergreaves.recipe.commands.UnitOfMeasureCommand;
import com.ibm.petergreaves.recipe.domain.Ingredient;
import com.ibm.petergreaves.recipe.domain.Notes;
import com.ibm.petergreaves.recipe.domain.Recipe;
import com.ibm.petergreaves.recipe.domain.UnitOfMeasure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.criteria.CriteriaBuilder;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class IngredientToIngredientCommandTest {

    IngredientToIngredientCommand ingredientConverter;

    IngredientCommand expected = null;
    final Long idExpected = 44L;
    final Long recipeID = 45499L;
    final Long uomID = 394L;
    final BigDecimal qty = BigDecimal.valueOf(3);
    final String desc = "flour";
    Recipe recipe;
    UnitOfMeasure uom;


    @BeforeEach
    void setup() {

        recipe = Recipe.builder().id(recipeID).build();
        uom = new UnitOfMeasure();
        uom.setDescription("Cup");
        uom.setId(uomID);
        expected = IngredientCommand.builder()
                .id(idExpected)
                .recipeID(recipe.getId())
                .quantity(qty)
                .description(desc)
                .uom(new UnitOfMeasureToUnitOfMeasureCommand().convert(uom))
                .build();

        ingredientConverter = new IngredientToIngredientCommand();

    }

    @Test
    void returnsNullForNull() {

        assertNull(ingredientConverter.convert(null));

    }

    @Test
    void returnsEmptyForEmpty() {

        assertNotNull(ingredientConverter.convert(Ingredient.builder().build()));

    }



    @Test
    void convert() {

        Ingredient ingredient = Ingredient
                .builder()
                .description(desc)
                .id(idExpected)
                .quantity(qty)
                .uom(uom)
                .recipe(recipe)
                .build();

        IngredientCommand converted = ingredientConverter.convert(ingredient);

        assertEquals(expected.getDescription(), converted.getDescription());
        assertEquals(expected.getQuantity(), converted.getQuantity());
        assertEquals(expected.getUom().getId(), converted.getUom().getId());
        assertEquals(expected.getId(), converted.getId());
    }
}

