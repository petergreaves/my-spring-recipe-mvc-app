package com.ibm.petergreaves.recipe.converters;

import com.ibm.petergreaves.recipe.commands.CategoryCommand;
import com.ibm.petergreaves.recipe.commands.IngredientCommand;
import com.ibm.petergreaves.recipe.commands.RecipeCommand;
import com.ibm.petergreaves.recipe.domain.Category;
import com.ibm.petergreaves.recipe.domain.Ingredient;
import com.ibm.petergreaves.recipe.domain.Recipe;
import com.sun.istack.Nullable;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class RecipeToRecipeCommand implements Converter<Recipe, RecipeCommand> {


    @Override
    public RecipeCommand convert(Recipe recipe) {
        if (recipe == null){
            return null;
        }

        RecipeCommand recipeCommand = RecipeCommand.builder()
                .id(recipe.getId())
                .title(recipe.getTitle())
                .description(recipe.getDescription())
                .directions(recipe.getDirections())
                .cookTime(recipe.getCookTime())
                .prepTime(recipe.getPrepTime())
                .servings(recipe.getServings())
                .difficulty(recipe.getDifficulty())
                .notes(new NotesToNotesCommand().convert((recipe.getNotes())))
                .image(recipe.getImage())
                .url(recipe.getUrl())
                .source(recipe.getSource())
                .build();

        Set<Ingredient> ingredientSet = recipe.getIngredients();

        if (ingredientSet!=null){

            IngredientToIngredientCommand converter = new IngredientToIngredientCommand();
            ingredientSet.forEach(i -> recipeCommand.addIngredient(converter.convert(i)));

        }

        Set<Category> categorySet = recipe.getCategories();

        if (categorySet!=null){

            CategoryToCategoryCommand converter = new CategoryToCategoryCommand();
            Set<CategoryCommand> cats = new HashSet<>();
            categorySet.forEach(c-> cats.add(converter.convert(c)));

            recipeCommand.setCategories(cats);
        }
        return recipeCommand;
    }
}
