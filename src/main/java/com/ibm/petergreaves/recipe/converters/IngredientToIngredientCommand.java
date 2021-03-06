package com.ibm.petergreaves.recipe.converters;

import com.ibm.petergreaves.recipe.commands.IngredientCommand;
import com.ibm.petergreaves.recipe.commands.UnitOfMeasureCommand;
import com.ibm.petergreaves.recipe.domain.Ingredient;
import com.sun.istack.Nullable;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class IngredientToIngredientCommand implements Converter<Ingredient, IngredientCommand> {

    @Override
    @Nullable
    @Synchronized
    public IngredientCommand convert(Ingredient ingredient) {
        if (ingredient == null){

            return null;
        }

        IngredientCommand command = IngredientCommand.builder()
                .id(ingredient.getId())
                .description(ingredient.getDescription())
                .uom(new UnitOfMeasureToUnitOfMeasureCommand().convert(ingredient.getUom()))
                .quantity(ingredient.getQuantity())
                .build();

        // just a check to if the ingred has an associated recipe

        if (ingredient.getRecipe()!=null){

            command.setRecipeID(ingredient.getRecipe().getId());
        }

        return command;

    }
}
