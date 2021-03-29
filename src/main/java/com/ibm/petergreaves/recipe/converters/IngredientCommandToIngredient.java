package com.ibm.petergreaves.recipe.converters;

import com.ibm.petergreaves.recipe.commands.IngredientCommand;
import com.ibm.petergreaves.recipe.domain.Ingredient;

import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class IngredientCommandToIngredient implements Converter<IngredientCommand, Ingredient> {

    @Override


    @Synchronized
    public Ingredient convert(IngredientCommand ingredientCommand) {

        if (ingredientCommand == null){

            return null;
        }


        Ingredient ingredient = new Ingredient();

        ingredient.setId(ingredientCommand.getId());
        ingredient.setDescription(ingredientCommand.getDescription());
        ingredient.setUom(new UnitOfMeasureCommandToUnitOfMeasure().convert(ingredientCommand.getUom()));
        ingredient.setQuantity(ingredientCommand.getQuantity());

        return ingredient;
    }
}
