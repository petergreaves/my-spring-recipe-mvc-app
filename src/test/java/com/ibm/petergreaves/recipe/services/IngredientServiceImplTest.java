package com.ibm.petergreaves.recipe.services;

import com.ibm.petergreaves.recipe.commands.IngredientCommand;
import com.ibm.petergreaves.recipe.converters.IngredientToIngredientCommand;
import com.ibm.petergreaves.recipe.converters.RecipeCommandToRecipe;
import com.ibm.petergreaves.recipe.converters.RecipeToRecipeCommand;
import com.ibm.petergreaves.recipe.domain.Ingredient;
import com.ibm.petergreaves.recipe.domain.Recipe;
import com.ibm.petergreaves.recipe.repositories.RecipeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.persistence.criteria.CriteriaBuilder;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class IngredientServiceImplTest {

    IngredientServiceImpl ingredientService;

    @Mock
    RecipeRepository recipeRepository;

    private AutoCloseable closeable;

    @BeforeEach
    public void openMocks() {
        closeable = MockitoAnnotations.openMocks(this);
        ingredientService = new IngredientServiceImpl(recipeRepository, new IngredientToIngredientCommand());
    }

    @AfterEach
    public void releaseMocks() throws Exception {
        closeable.close();
    }

    @Test
    void getIngredientForRecipeIDAndRecipeIDHappyPath(){

        Recipe recipe = new Recipe();

        recipe.setId(9L);
        Ingredient i1 = Ingredient.builder().id(1L).build();
        Ingredient i2 = Ingredient.builder().id(2L).build();
        Ingredient i3 = Ingredient.builder().id(3L).build();
        Set<Ingredient> ingredientSet = new HashSet<>();

        ingredientSet.add(i1);
        ingredientSet.add(i2);
        ingredientSet.add(i3);

        ingredientSet.forEach(i -> recipe.addIngredient(i));

        Optional<Recipe> recipeOptional = Optional.of(recipe);

        when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);

        IngredientCommand ic = ingredientService.findByRecipeIdAndIngredientId(9L, 1L);
        assertNotNull(ic);;
        assertTrue(ic.getId()==1L);
        assertTrue(ic.getRecipeID() == 9L);
        verify(recipeRepository).findById(9L);


    }

}
