package com.ibm.petergreaves.recipe.converters;

import com.ibm.petergreaves.recipe.commands.CategoryCommand;
import com.ibm.petergreaves.recipe.commands.IngredientCommand;
import com.ibm.petergreaves.recipe.commands.NotesCommand;
import com.ibm.petergreaves.recipe.commands.RecipeCommand;
import com.ibm.petergreaves.recipe.domain.*;
import org.junit.jupiter.api.BeforeEach;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RecipeToRecipeCommandTest {

    final Long recipeID = 303L;
    final String title = "Title for the recipe";
    final String url = "https://a.url.com";
    final String directions = "here are the directions";
    final String description = "here's the description";
    final Integer cookTime = 60;
    final Integer prepTime = 30;
    final Difficulty diff = Difficulty.HARD;
    final Byte[] image = new Byte['x'];
    final int servings =2;
    final String source = "from here";
    final String recipeNotesText = "here are the recipe notes";
    final Long noteID = 390L;


    RecipeCommand expected;

    NotesCommand recipeNotes;
    Set<Ingredient> ingredients;
    Set<Category> categories;

    RecipeToRecipeCommand recipeConverter;

    @BeforeEach
    void setup(){


        recipeNotes= new NotesCommand();
        recipeNotes.setId(noteID);
        recipeNotes.setRecipeNotes(recipeNotesText);

        Set<IngredientCommand> ingredients = new HashSet<>();
        ingredients.add(
                IngredientCommand.builder().id(1L).build());
        ingredients.add(
                IngredientCommand.builder().id(2L).build());

        Set<CategoryCommand>categories = new HashSet<>();
        categories.add(
                CategoryCommand.builder().id(44L).description("American").build());
        categories.add(
                CategoryCommand.builder().id(44L).description("Mexican").build());

        expected = RecipeCommand.builder()
                .id(recipeID)
                .title(title)
                .description(description)
                .servings(servings)
                .url(url)
                .image(image)
                .prepTime(prepTime)
                .cookTime(cookTime)
                .notes(recipeNotes)
                .ingredients(ingredients)
                .categories(categories)
                .build();


        recipeConverter = new RecipeToRecipeCommand();

    }




}
