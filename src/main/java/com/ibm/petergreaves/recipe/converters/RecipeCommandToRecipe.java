package com.ibm.petergreaves.recipe.converters;


import com.ibm.petergreaves.recipe.commands.CategoryCommand;
import com.ibm.petergreaves.recipe.commands.IngredientCommand;
import com.ibm.petergreaves.recipe.commands.NotesCommand;
import com.ibm.petergreaves.recipe.commands.RecipeCommand;
import com.ibm.petergreaves.recipe.domain.Category;
import com.ibm.petergreaves.recipe.domain.Recipe;

import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class RecipeCommandToRecipe implements Converter <RecipeCommand, Recipe> {


    @Synchronized
    @Override
    public Recipe convert(RecipeCommand recipeCommand) {
        if (recipeCommand == null){

            return null;
        }

        Recipe recipe = Recipe.builder()
                .id(recipeCommand.getId())
                .directions(recipeCommand.getDirections())
                .cookTime(recipeCommand.getCookTime())
                .prepTime(recipeCommand.getPrepTime())
                .difficulty(recipeCommand.getDifficulty())
                .image(recipeCommand.getImage())
                .url(recipeCommand.getUrl())
                .source(recipeCommand.getSource())
                .notes(new NotesCommandToNotes().convert(recipeCommand.getNotes()))
                .servings(recipeCommand.getServings())
                .title(recipeCommand.getTitle())
                .description(recipeCommand.getDescription())
                .build();

        Set<IngredientCommand> ingredientCommandSet = recipeCommand.getIngredients();

        if (ingredientCommandSet!=null){

            IngredientCommandToIngredient converter = new IngredientCommandToIngredient();
           ingredientCommandSet.forEach(i -> recipe.addIngredient(converter.convert(i)));

        }

        Set<CategoryCommand> categoryCommandSet = recipeCommand.getCategories();

        if (categoryCommandSet!=null){

            CategoryCommandToCategory converter = new CategoryCommandToCategory();
            Set<Category> cats = new HashSet<>();
            categoryCommandSet.forEach(c-> cats.add(converter.convert(c)));

            recipe.setCategories(cats);
        }

        return  recipe;
    }
}
