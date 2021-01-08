package com.ibm.petergreaves.recipe.services;

import com.ibm.petergreaves.recipe.domain.Recipe;
import com.ibm.petergreaves.recipe.repositories.RecipeRepository;
import lombok.Setter;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
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
        recipeService = new RecipeServiceImpl(recipeRepository);
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
}
