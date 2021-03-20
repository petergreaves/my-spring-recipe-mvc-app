package com.ibm.petergreaves.recipe.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;


import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"recipe"})
public class Ingredient {


    @Builder.Default
    private String id = UUID.randomUUID().toString();

    private Recipe recipe;
    private String description;
    private BigDecimal quantity;

    @DBRef
    private UnitOfMeasure uom;


}
