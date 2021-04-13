package com.ibm.petergreaves.recipe.controllers;

import com.ibm.petergreaves.recipe.domain.Recipe;
import com.ibm.petergreaves.recipe.services.RecipeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import reactor.core.publisher.Flux;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

class HomePageControllerTest {


    @Mock
    RecipeService recipeService;


    HomePageController controller;

    @Mock
    Model model;


    private AutoCloseable closeable;

    @BeforeEach
    public void openMocks() {
        closeable = MockitoAnnotations.openMocks(this);
        controller = new HomePageController(recipeService);

    }

    @AfterEach
    public void releaseMocks() throws Exception {
        closeable.close();
    }


    @Test
    void doHomeReturnsIndex() {


        Mockito.when(recipeService.getRecipes()).thenReturn(Flux.just(Recipe.builder().build()));
        String page = controller.doHome(model);
        assertEquals(page, "index");

    }

    @Test
    void testMVC() throws Exception{

        Mockito.when(recipeService.getRecipes()).thenReturn(Flux.just(Recipe.builder().build()));

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        mockMvc.perform(get("/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("index"));

    }



    @Test
    void modelHasRecipes(){

        //given

        Recipe r1 = new Recipe();
        r1.setId("1");
        Recipe r2 = new Recipe();
        r2.setId("2");
        Set<Recipe> recipes = new HashSet<>();
        recipes.add(r1);
        recipes.add(r2);
        Mockito.when(recipeService.getRecipes()).thenReturn(Flux.fromIterable(recipes));
        ArgumentCaptor<List<Recipe>> argumentCaptor = ArgumentCaptor.forClass(List.class);

        //when
        controller.doHome(model);
        //then
        verify(model, times(1)).addAttribute(eq("recipes"), argumentCaptor.capture());
        verify(recipeService, times(1)).getRecipes();

        List<Recipe> fromController = argumentCaptor.getValue();
        assertEquals(fromController.size(),2);


    }
}
