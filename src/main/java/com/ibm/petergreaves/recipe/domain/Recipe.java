package com.ibm.petergreaves.recipe.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Recipe {

    @Id
    private String id;


    private Byte[] image;
    private Integer cookTime;
    private Integer prepTime;
    private Integer servings;
    private String source;
    private String url;
    private String directions;
    private String description;
    private String title;




    @Builder.Default
    @DBRef
    private Set<Category> categories= new HashSet<>();


    private Difficulty difficulty;


    @Builder.Default
    private Set<Ingredient> ingredients = new HashSet<>();


    private Notes notes;

    public void setNotes(Notes notes) {
        this.notes = notes;
       // notes.setRecipe(this);
    }



    public Recipe addIngredient(Ingredient i){

     //   i.setRecipe(this);
        this.ingredients.add(i);
        return this;
    }


}
