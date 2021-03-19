package com.ibm.petergreaves.recipe.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;


import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"recipe"})
public class Ingredient {


    @Id
    private String id;


    private Recipe recipe;
    private String description;
    private BigDecimal quantity;

    @DBRef
    private UnitOfMeasure uom;


}
