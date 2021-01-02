package com.ibm.petergreaves.recipe.domain;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class Ingredient implements Comparable<Ingredient>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Recipe recipe;
    private String description;
    private BigDecimal quantity;

    @OneToOne(fetch = FetchType.EAGER)
    private UnitOfMeasure oum;


    public Ingredient(){}

    public Ingredient(String description, BigDecimal quantity, UnitOfMeasure oum) {
        this.description = description;
        this.quantity = quantity;
        this.oum = oum;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public UnitOfMeasure getOum() {
        return oum;
    }

    public void setOum(UnitOfMeasure oum) {
        this.oum = oum;
    }


    @Override
    public String toString() {
        return "Ingredient{" +
                "description='" + description + '\'' +
                ", quantity=" + quantity +
                ", oum=" + oum +
                '}';
    }

    @Override
    public int compareTo(Ingredient o) {
        return o.getDescription().compareTo(this.getDescription());
    }
}
