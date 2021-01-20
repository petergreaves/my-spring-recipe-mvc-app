package com.ibm.petergreaves.recipe.commands;

import com.ibm.petergreaves.recipe.domain.Recipe;
import com.ibm.petergreaves.recipe.domain.UnitOfMeasure;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IngredientCommand {

    private Long id;
    private String description;
    private BigDecimal quantity;
    private UnitOfMeasureCommand uom;
}
