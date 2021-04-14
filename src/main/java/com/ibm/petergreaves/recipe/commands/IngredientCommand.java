package com.ibm.petergreaves.recipe.commands;


import lombok.*;


import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IngredientCommand {

    private String id;
    private String recipeID;

    @NotBlank
    private String description;

    @Min(1)
    @NotNull
    private BigDecimal quantity;

    @NotNull
    private UnitOfMeasureCommand uom;
}
