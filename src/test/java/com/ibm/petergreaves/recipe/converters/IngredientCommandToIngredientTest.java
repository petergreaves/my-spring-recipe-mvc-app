package com.ibm.petergreaves.recipe.converters;

import com.ibm.petergreaves.recipe.commands.IngredientCommand;
import com.ibm.petergreaves.recipe.commands.NotesCommand;
import com.ibm.petergreaves.recipe.commands.UnitOfMeasureCommand;
import com.ibm.petergreaves.recipe.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class IngredientCommandToIngredientTest {

    IngredientCommandToIngredient ingredientConverter;

    Ingredient expected=null;
    final String idExpected="44";
    final String recipeID="45499";
    final String uomID="394";
    final BigDecimal qty = BigDecimal.valueOf(3);
    final String desc="flour";
    Recipe recipe;
    UnitOfMeasure uom;


    @BeforeEach
    void setup(){

        recipe=Recipe.builder().id(recipeID).build();
        uom =new UnitOfMeasure();
        uom.setDescription("Cup");
        uom.setId(uomID);
        expected = new Ingredient();

        expected.setDescription(desc);
        expected.setId(idExpected);
        expected.setQuantity(qty);
        expected.setUom(uom);

        ingredientConverter = new IngredientCommandToIngredient();

    }

    @Test
    void returnsNullForNull(){

        assertNull(ingredientConverter.convert(null));

    }

    @Test
    void returnsEmptyForEmpty(){

        assertNotNull(ingredientConverter.convert(IngredientCommand.builder().build()));

    }


    @Test
    void convert() {


       UnitOfMeasure uom =new UnitOfMeasure();
        uom.setDescription("Cup");
        uom.setId(uomID);

        IngredientCommand command = IngredientCommand
                .builder()
                .description(desc)
                .id(idExpected)
                .quantity(qty)
                .uom(new UnitOfMeasureToUnitOfMeasureCommand().convert(uom))
                .build();

        Ingredient converted = ingredientConverter.convert(command);

        assertEquals(expected.getDescription(), converted.getDescription());
        assertEquals(expected.getQuantity(), converted.getQuantity());
      //  assertEquals(expected.getRecipe().getId(), converted.getRecipe().getId());
        assertEquals(expected.getUom().getId(), converted.getUom().getId());
        assertEquals(expected.getId(), converted.getId());

    }
}
