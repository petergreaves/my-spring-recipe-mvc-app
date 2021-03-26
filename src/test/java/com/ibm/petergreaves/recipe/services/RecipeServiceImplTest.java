package com.ibm.petergreaves.recipe.services;

import com.ibm.petergreaves.recipe.converters.RecipeCommandToRecipe;
import com.ibm.petergreaves.recipe.converters.RecipeToRecipeCommand;
import com.ibm.petergreaves.recipe.domain.Recipe;
import com.ibm.petergreaves.recipe.exceptions.NotFoundException;
import com.ibm.petergreaves.recipe.repositories.RecipeRepository;
import com.ibm.petergreaves.recipe.repositories.reactive.RecipeReactiveRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RecipeServiceImplTest {

    RecipeServiceImpl recipeService;

    @Mock
    RecipeReactiveRepository recipeRepository;

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
        rec1.setId("22");
        Recipe rec2 = new Recipe();
        rec2.setId("23");

        Set<Recipe> recipes= new HashSet<>();
        recipes.add(rec1);
        recipes.add(rec2);

        when(recipeRepository.findAll()).thenReturn(Flux.just(rec1, rec2));

        assertEquals(recipeService.getRecipes().count().block().longValue(), 2);
        verify(recipeRepository, times(1)).findAll();


      //  Mock
    }

    @Test
    void getRecipeByID(){
        Recipe rec1 = new Recipe();

        String id = "2";
        rec1.setId(id);

        final ArgumentCaptor<Recipe> captor = ArgumentCaptor.forClass(Recipe.class);

        when(recipeRepository.findById(id)).thenReturn(Mono.just(rec1));

        Recipe recipeReturned = recipeService.getRecipeByID(id).block();

        assertEquals(recipeReturned.getId(), rec1.getId());
        verify(recipeRepository, times(1)).findById(anyString());
    }

    @Test
    void getRecipeByIDWithNotFoundThrowException(){

        String id = "1002";

        when(recipeRepository.findById(id)).thenThrow(NotFoundException.class);
        assertThrows(RuntimeException.class,() -> { recipeService.getRecipeByID(id);} );
        verify(recipeRepository, times(1)).findById(anyString());
    }

    @Test
    void deleteRecipeByID(){


        when(recipeRepository.deleteById(anyString())).thenReturn(Mono.empty());
        //given
        String id = "2";

        //when
        recipeService.deleteByID(id);

        //then
        verify(recipeRepository, times(1)).deleteById(id);

    }
}
