package com.ibm.petergreaves.recipe.services;

import com.ibm.petergreaves.recipe.commands.IngredientCommand;
import com.ibm.petergreaves.recipe.converters.IngredientCommandToIngredient;
import com.ibm.petergreaves.recipe.converters.IngredientToIngredientCommand;
import com.ibm.petergreaves.recipe.converters.RecipeCommandToRecipe;
import com.ibm.petergreaves.recipe.converters.RecipeToRecipeCommand;
import com.ibm.petergreaves.recipe.domain.Ingredient;
import com.ibm.petergreaves.recipe.domain.Recipe;
import com.ibm.petergreaves.recipe.repositories.RecipeRepository;
import com.ibm.petergreaves.recipe.repositories.UnitOfMeasureRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class IngredientServiceImplTest {

    IngredientServiceImpl ingredientService;

    @Mock
    RecipeRepository recipeRepository;

    @Mock
    UnitOfMeasureRepository unitOfMeasureRepository;

    private AutoCloseable closeable;

    @BeforeEach
    public void openMocks() {
        closeable = MockitoAnnotations.openMocks(this);
        ingredientService = new IngredientServiceImpl(recipeRepository, new IngredientCommandToIngredient(),new IngredientToIngredientCommand(),  unitOfMeasureRepository);
    }

    @AfterEach
    public void releaseMocks() throws Exception {
        closeable.close();
    }

    @Test
    void getIngredientForRecipeIDAndRecipeIDHappyPath(){

        Recipe recipe = new Recipe();

        recipe.setId("9");
        Ingredient i1 = Ingredient.builder().id("1").build();
        Ingredient i2 = Ingredient.builder().id("2").build();
        Ingredient i3 = Ingredient.builder().id("3").build();
        Set<Ingredient> ingredientSet = new HashSet<>();

        ingredientSet.add(i1);
        ingredientSet.add(i2);
        ingredientSet.add(i3);

        ingredientSet.forEach(i -> recipe.addIngredient(i));

        Optional<Recipe> recipeOptional = Optional.of(recipe);

        when(recipeRepository.findById(anyString())).thenReturn(recipeOptional);

        IngredientCommand ic = ingredientService.findByRecipeIdAndIngredientId("9", "1");
        assertNotNull(ic);;
        assertTrue(ic.getId().equals("1"));
        assertEquals(ic.getRecipeID(),  "9");
        verify(recipeRepository).findById("9");


    }

    @Test
    void doDeleteByRecipeIDAndIngredientID(){

        Recipe recipe = Recipe.builder().id("33").build();

        Ingredient i1 = Ingredient.builder().id("1").build();
        Ingredient i2 = Ingredient.builder().id("2").build();
        Ingredient i3 = Ingredient.builder().id("3").build();
        Set<Ingredient> ingredientSet = new HashSet<>();

        ingredientSet.add(i1);
        ingredientSet.add(i2);
        ingredientSet.add(i3);

        ingredientSet.forEach(i -> recipe.addIngredient(i));

        Optional<Recipe> recipeOptional = Optional.of(recipe);

        when(recipeRepository.findById(anyString())).thenReturn(recipeOptional);

        IngredientCommand command =IngredientCommand.builder().id("2").recipeID("33").build();

        ingredientService.removeIngredientCommand(command);

        verify(recipeRepository, times(1)).findById(anyString());
        verify(recipeRepository, times(1)).save(any(Recipe.class));

    }

}
