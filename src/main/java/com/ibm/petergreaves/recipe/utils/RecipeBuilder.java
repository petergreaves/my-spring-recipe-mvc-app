package com.ibm.petergreaves.recipe.utils;

import com.ibm.petergreaves.recipe.domain.Difficulty;
import com.ibm.petergreaves.recipe.domain.Ingredient;
import com.ibm.petergreaves.recipe.domain.Notes;
import com.ibm.petergreaves.recipe.domain.Recipe;
import org.springframework.context.annotation.Bean;

import java.util.Set;

public class RecipeBuilder {

    private static RecipeBuilder builder;
    private final Recipe recipe;

    public RecipeBuilder(){

       if (null==builder){

           builder=new RecipeBuilder();

       }         recipe = new Recipe();

    }

    public RecipeBuilder withDirections(String dirs){

        recipe.setSource(dirs);
        return builder;

    }

    public RecipeBuilder withTitle(String title){

        recipe.setTitle(title);
        return builder;

    }

    public RecipeBuilder withSource(String source){

        recipe.setSource(source);
        return builder;

    }

    public RecipeBuilder withServings(Integer servings){

        recipe.setServings(servings);
        return builder;

    }

    public RecipeBuilder withPrepTime(Integer prepTime){

        recipe.setPrepTime(prepTime);
        return builder;

    }

    public RecipeBuilder withCookTime(Integer cookTime){

        recipe.setCookTime(cookTime);
        return builder;

    }


    public RecipeBuilder withDifficulty(Difficulty diff){

        recipe.setDifficulty(diff);
        return builder;

    }


    public RecipeBuilder withNotes(Notes notes){

        recipe.setNotes(notes);
        return builder;

    }


    public RecipeBuilder withIngredients(Set<Ingredient> ingredients){

        recipe.setIngredients(ingredients);
        return builder;

    }

    public RecipeBuilder withURL(String url){

        recipe.setUrl(url);
        return builder;

    }

    public RecipeBuilder withImage(Byte[] image){

        recipe.setImage(image);
        return builder;

    }

    public Recipe build(){

        return recipe;
    }
}
