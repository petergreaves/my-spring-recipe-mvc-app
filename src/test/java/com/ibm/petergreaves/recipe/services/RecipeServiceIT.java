package com.ibm.petergreaves.recipe.services;

import com.ibm.petergreaves.recipe.commands.RecipeCommand;
import com.ibm.petergreaves.recipe.converters.RecipeCommandToRecipe;
import com.ibm.petergreaves.recipe.converters.RecipeToRecipeCommand;
import com.ibm.petergreaves.recipe.domain.Recipe;
import com.ibm.petergreaves.recipe.repositories.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith(SpringExtension.class)
@SpringBootTest
public class RecipeServiceIT {


    @Autowired
    RecipeService recipeService;

    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    RecipeCommandToRecipe recipeCommandToRecipe;

    @Autowired
    RecipeToRecipeCommand recipeToRecipeCommand;


    @Test
    @Transactional
    void testSavedProperties(){

        final String newTitle = "new title for the recipe";

        Iterable<Recipe> allRecipes = recipeRepository.findAll();

        Recipe testRecipe =allRecipes.iterator().next();
        String testRecipeID = testRecipe.getId();
        RecipeCommand command = recipeToRecipeCommand.convert(testRecipe);

        RecipeCommand savedCommand=recipeService.saveRecipeCommand(command);
        assertEquals(command.getTitle(),  savedCommand.getTitle());
        assertEquals(command.getDescription(),  savedCommand.getDescription());
        assertEquals(command.getSource(),  savedCommand.getSource());
        assertEquals(command.getServings(),  savedCommand.getServings());
        assertEquals(command.getPrepTime(),  savedCommand.getPrepTime());
        assertEquals(command.getCookTime(),  savedCommand.getCookTime());
        assertEquals(command.getIngredients().size(), savedCommand.getIngredients().size());
        assertEquals(command.getCategories().size(), savedCommand.getCategories().size());

    }



}
