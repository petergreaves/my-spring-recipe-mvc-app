package com.ibm.petergreaves.recipe.commands;

import com.ibm.petergreaves.recipe.domain.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeCommand {

    private Long id;
    private Byte[] image;
    private Integer cookTime;
    private Integer prepTime;
    private Integer servings;
    private String source;
    private String url;
    private String directions;
    private String description;
    private String title;
    private Set<CategoryCommand> categories=new HashSet<>();
    private Difficulty difficulty;
    private Set<IngredientCommand> ingredients =new HashSet<>();
    private NotesCommand notes;

    public RecipeCommand addIngredient(IngredientCommand i){

        if (ingredients==null){

            ingredients= new HashSet<>();
        }
        this.ingredients.add(i);
        return this;
    }
}
