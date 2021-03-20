package com.ibm.petergreaves.recipe.commands;

import com.ibm.petergreaves.recipe.domain.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeCommand {

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
    private List<CategoryCommand> categories=new ArrayList();
    private Difficulty difficulty;
    @Builder.Default
    private List<IngredientCommand> ingredients =new ArrayList<>();
    private NotesCommand notes;

    public RecipeCommand addIngredient(IngredientCommand i){

        if (ingredients==null){

            ingredients= new ArrayList<>();
        }
        this.ingredients.add(i);
        return this;
    }
}
