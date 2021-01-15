package com.ibm.petergreaves.recipe.controllers;

import com.ibm.petergreaves.recipe.domain.Recipe;
import com.ibm.petergreaves.recipe.services.RecipeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

class RecipeControllerTest {


    @Mock
    RecipeService recipeService;

    @InjectMocks
    RecipeController controller;

    @Mock
    Model model;

    private AutoCloseable closeable;

    @BeforeEach
    public void openMocks() {
        closeable = MockitoAnnotations.openMocks(this);
        controller = new RecipeController(recipeService);

    }

    @AfterEach
    public void releaseMocks() throws Exception {
        closeable.close();
    }

    @Test
    void getRecipeByID() throws Exception{
        //given
        Long id = 33L;
        Recipe r1 = new Recipe();
        r1.setId(id);


        ArgumentCaptor<Recipe> argumentCaptor = ArgumentCaptor.forClass(Recipe.class);
        when(recipeService.getRecipeByID(id)).thenReturn(r1);

        //when
        controller.getRecipeByID(model, id+"");


        //then
        verify(model, times(1)).addAttribute(eq("recipe"), argumentCaptor.capture());
        verify(recipeService, times(1)).getRecipeByID(anyLong());

        Recipe recipeFromCaptor= argumentCaptor.getValue();
        assertEquals(recipeFromCaptor.getId(),id);

    }

    @Test
    void testMVC() throws Exception{

        when(recipeService.getRecipeByID(33L)).thenReturn(any(Recipe.class));
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        mockMvc.perform(get("/recipe/33/show"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("recipe/show"));

        verify(recipeService, times(1)).getRecipeByID(anyLong());

    }

}
