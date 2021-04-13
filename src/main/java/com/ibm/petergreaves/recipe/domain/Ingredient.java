package com.ibm.petergreaves.recipe.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;


import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class Ingredient {


    private String id = UUID.randomUUID().toString();

    private Recipe recipe;
    private String description;
    private BigDecimal quantity;
    private UnitOfMeasure uom;

    public Ingredient() {

    }

    public Ingredient(String description, BigDecimal amount, UnitOfMeasure uom) {
        this.description = description;
        this.quantity = amount;
        this.uom = uom;
    }

    public Ingredient(String description, BigDecimal amount, UnitOfMeasure uom, Recipe recipe) {
        this.description = description;
        this.quantity = amount;
        this.uom = uom;
    }


}
