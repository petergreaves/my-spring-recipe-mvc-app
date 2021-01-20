package com.ibm.petergreaves.recipe.services;

import com.ibm.petergreaves.recipe.converters.RecipeCommandToRecipe;
import com.ibm.petergreaves.recipe.converters.RecipeToRecipeCommand;
import com.ibm.petergreaves.recipe.domain.Recipe;
import com.ibm.petergreaves.recipe.repositories.RecipeRepository;
import lombok.Setter;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RecipeServiceImplTest {

    RecipeServiceImpl recipeService;

    @Mock
    RecipeRepository recipeRepository;

    private AutoCloseable closeable;

    @BeforeEach
    public void openMocks() {
        closeable = MockitoAnnotations.openMocks(this);
        recipeService = new RecipeServiceImpl(recipeRepository, new RecipeToRecipeCommand(), new RecipeCommandToRecipe());
    }

    @AfterEach
    public void releaseMocks() throws Exception {
        closeable.close();
    }

    @Test
    void getRecipes() {
        Recipe rec1 = new Recipe();
        rec1.setId(22L);
        Recipe rec2 = new Recipe();
        rec2.setId(23L);

        Set<Recipe> recipes= new HashSet<>();
        recipes.add(rec1);
        recipes.add(rec2);

        when(recipeRepository.findAll()).thenReturn(recipes);

        assertEquals(recipeService.getRecipes().size(), 2);
        verify(recipeRepository, times(1)).findAll();


      //  Mock
    }

    @Test
    void getRecipeByID(){
        Recipe rec1 = new Recipe();

        Long id = 2L;
        rec1.setId(id);

        final ArgumentCaptor<Recipe> captor = ArgumentCaptor.forClass(Recipe.class);

        when(recipeRepository.findById(id)).thenReturn(Optional.of(rec1));

        Recipe recipeReturned = recipeService.getRecipeByID(id);

        assertEquals(recipeReturned.getId(), rec1.getId());
        verify(recipeRepository, times(1)).findById(anyLong());
    }

    @Test
    void getRecipeByIDWithNotFoundThrowException(){

        Long id = 1002L;

        when(recipeRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class,() -> { recipeService.getRecipeByID(id);} );
        verify(recipeRepository, times(1)).findById(anyLong());
    }

    @Test
    void deleteRecipeByID(){

        //given
        Long id = 2L;

        //when
        recipeService.deleteByID(id);

        //then
        verify(recipeRepository, times(1)).deleteById(id);

    }
}
