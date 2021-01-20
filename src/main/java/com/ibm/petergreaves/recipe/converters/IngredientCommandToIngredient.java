package com.ibm.petergreaves.recipe.converters;

import com.ibm.petergreaves.recipe.commands.IngredientCommand;
import com.ibm.petergreaves.recipe.domain.Ingredient;
import com.sun.istack.Nullable;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class IngredientCommandToIngredient implements Converter<IngredientCommand, Ingredient> {

    @Override
    @Nullable
    @Synchronized
    public Ingredient convert(IngredientCommand ingredientCommand) {

        if (ingredientCommand == null){

            return null;
        }

        return Ingredient.builder()
                .id(ingredientCommand.getId())
                .description(ingredientCommand.getDescription())
                .uom(new UnitOfMeasureCommandToUnitOfMeasure().convert(ingredientCommand.getUom()))
                .quantity(ingredientCommand.getQuantity())
                .build();

    }
}
