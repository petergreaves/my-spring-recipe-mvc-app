package com.ibm.petergreaves.recipe.converters;

import com.ibm.petergreaves.recipe.commands.CategoryCommand;
import com.ibm.petergreaves.recipe.commands.IngredientCommand;
import com.ibm.petergreaves.recipe.commands.NotesCommand;
import com.ibm.petergreaves.recipe.commands.RecipeCommand;
import com.ibm.petergreaves.recipe.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.convert.converter.Converter;


import java.io.DataInput;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RecipeCommandToRecipeTest {


    final String recipeID="303";
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
    final String noteID = "390";


    Recipe expected;
    Notes recipeNotes;
    Set<Ingredient> ingredients;
    Set<Category> categories;

    RecipeCommandToRecipe recipeConverter;

    @BeforeEach
    void setup(){


        recipeNotes= new Notes();
        recipeNotes.setId(noteID);
        recipeNotes.setRecipeNotes(recipeNotesText);

        Set<Ingredient> ingredients = new HashSet<>();
        ingredients.add(
                Ingredient.builder().id("1").build());
        ingredients.add(
                Ingredient.builder().id("2").build());

        categories = new HashSet<>();
        categories.add(
                Category.builder().id("44").description("American").build());
        categories.add(
                Category.builder().id("44").description("Mexican").build());

        expected = Recipe.builder()
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

        recipeNotes.setRecipe(expected);
        recipeConverter = new RecipeCommandToRecipe();

    }

    @Test
    void returnsNullForNull(){

        assertNull(recipeConverter.convert(null));

    }

    @Test
    void returnsEmptyForEmpty(){

        assertNotNull(recipeConverter.convert(RecipeCommand.builder().build()));

    }

    @Test
    void convert() {


        Set<CategoryCommand> categories = new HashSet<>();
        Set<IngredientCommand> ingredients = new HashSet<>();
        ingredients.add(
                IngredientCommand.builder().id("1").build());
        ingredients.add(
                IngredientCommand.builder().id("2").build());

        categories = new HashSet<>();
        categories.add(
                CategoryCommand.builder().id("44").description("American").build());
        categories.add(
                CategoryCommand.builder().id("44").description("Mexican").build());


        RecipeCommand recipeCommand = RecipeCommand.builder()
                .id(recipeID)
                .title(title)
                .description(description)
                .servings(servings)
                .url(url)
                .image(image)
                .prepTime(prepTime)
                .cookTime(cookTime)
                .notes(new NotesToNotesCommand().convert(recipeNotes))
                .categories(categories)
                 .ingredients(ingredients)
                .build();



        Recipe fromConverter = recipeConverter.convert(recipeCommand);
        assertEquals(fromConverter.getId(), expected.getId());
        assertEquals(fromConverter.getTitle(), expected.getTitle());
        assertEquals(fromConverter.getCategories().size(), expected.getCategories().size());
        assertEquals(fromConverter.getCookTime(), expected.getCookTime());
        assertEquals(fromConverter.getPrepTime(), expected.getPrepTime());
        assertEquals(fromConverter.getDescription(), expected.getDescription());
        assertEquals(fromConverter.getTitle(), expected.getTitle());
        assertEquals(fromConverter.getSource(), expected.getSource());
        assertEquals(fromConverter.getUrl(), expected.getUrl());
        assertEquals(fromConverter.getDirections(), expected.getDirections());
        assertEquals(fromConverter.getNotes().getId(), expected.getNotes().getId());
        assertEquals(fromConverter.getNotes().getRecipeNotes(), expected.getNotes().getRecipeNotes());
        assertTrue(fromConverter.getCategories().size() > 0);
        assertEquals(fromConverter.getCategories().size(), expected.getCategories().size());
       assertTrue(fromConverter.getIngredients().size() > 0);
       assertEquals(fromConverter.getIngredients().size(), expected.getIngredients().size());



    }
}
