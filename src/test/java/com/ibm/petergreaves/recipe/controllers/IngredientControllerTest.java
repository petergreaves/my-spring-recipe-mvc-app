package com.ibm.petergreaves.recipe.controllers;

import com.ibm.petergreaves.recipe.commands.IngredientCommand;
import com.ibm.petergreaves.recipe.commands.RecipeCommand;
import com.ibm.petergreaves.recipe.commands.UnitOfMeasureCommand;
import com.ibm.petergreaves.recipe.services.IngredientService;
import com.ibm.petergreaves.recipe.services.RecipeService;
import com.ibm.petergreaves.recipe.services.UnitOfMeasureService;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

class IngredientControllerTest {


    @Mock
    IngredientService ingredientService;

    @Mock
    UnitOfMeasureService unitOfMeasureService;

    @Mock
    RecipeService recipeService;


    @Mock
    Model model;


    @InjectMocks
    IngredientController controller;

    private RecipeCommand recipeCommand;

    private final String ingredDesc1 = "ingred1 desc";
    private final String ingredDesc2 = "ingred2 desc";


    private AutoCloseable closeable;

    @BeforeEach
    public void openMocks() {
        closeable = MockitoAnnotations.openMocks(this);
        controller = new IngredientController(ingredientService, recipeService, unitOfMeasureService);
        recipeCommand = new RecipeCommand();
        recipeCommand.setId("99");

        IngredientCommand i1 = IngredientCommand.builder().id("1").description(ingredDesc1).build();
        IngredientCommand i2 = IngredientCommand.builder().id("2").description(ingredDesc2).build();

        recipeCommand.addIngredient(i1);
        recipeCommand.addIngredient(i2);
    }

    @AfterEach
    public void releaseMocks() throws Exception {
        closeable.close();
    }


    @Test
    void getIngredientsForRecipeID() {

        ArgumentCaptor<RecipeCommand> argumentCaptor = ArgumentCaptor.forClass(RecipeCommand.class);

        when(recipeService.findRecipeCommandByID(anyString())).thenReturn(recipeCommand);
        controller.getIngredientsForRecipeID(model, "9");
        verify(model, times(1)).addAttribute(eq("recipe"), argumentCaptor.capture());

        RecipeCommand commandFromController = argumentCaptor.getValue();
        List<IngredientCommand> ingredients = commandFromController.getIngredients();
        assertEquals(commandFromController.getId(),"99");
        assertEquals(ingredients.size(),2);
        verify(recipeService, times(1)).findRecipeCommandByID(anyString());

    }
    @Test
    void deleteIngredientByRecipeIDAndIngredientID() throws Exception{

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        IngredientCommand i1 = IngredientCommand.builder().id("3").description(ingredDesc1).recipeID("2").build();

        when(ingredientService.findByRecipeIdAndIngredientId(anyString(),anyString())).thenReturn(Mono.just(i1));
        when(ingredientService.removeIngredientCommand(any())).thenReturn(Mono.empty());

      //then
        mockMvc.perform(get("/recipe/2/ingredients/3/delete")
        )
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/recipe/2/ingredients"));

        verify(ingredientService, times(1)).findByRecipeIdAndIngredientId(anyString(), anyString());
        verify(ingredientService, times(1)).removeIngredientCommand(i1);
    }

    @Test
    void getIngredientByID() {

        ArgumentCaptor<RecipeCommand> argumentCaptor = ArgumentCaptor.forClass(RecipeCommand.class);

        when(recipeService.findRecipeCommandByID(anyString())).thenReturn(recipeCommand);
        controller.getIngredientsForRecipeID(model, "9");
        verify(model, times(1)).addAttribute(eq("recipe"), argumentCaptor.capture());

        RecipeCommand commandFromController = argumentCaptor.getValue();
        List<IngredientCommand> ingredients = commandFromController.getIngredients();
        IngredientCommand i1FromRecipe = ingredients.stream().filter(ic -> ic.getId()=="1").findFirst().get();
        assertEquals(i1FromRecipe.getDescription(), ingredDesc1);
        verify(recipeService, times(1)).findRecipeCommandByID(anyString());

    }

    @Test
    void recipeHasNoIngredients() {

        ArgumentCaptor<RecipeCommand> argumentCaptor = ArgumentCaptor.forClass(RecipeCommand.class);
        recipeCommand.setIngredients(new ArrayList<IngredientCommand>());
        when(recipeService.findRecipeCommandByID(anyString())).thenReturn(recipeCommand);
        controller.getIngredientsForRecipeID(model, ""+9L);
        verify(model, times(1)).addAttribute(eq("recipe"), argumentCaptor.capture());

        RecipeCommand commandFromController = argumentCaptor.getValue();

        assertTrue(recipeCommand.getIngredients().size() == 0);
        verify(recipeService, times(1)).findRecipeCommandByID(anyString());

    }

    @Test
    void testGetIngredientsMVC() throws Exception{

            MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
            mockMvc.perform(get("/recipe/99/ingredients"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.view().name("recipe/ingredient/list"));
             verify(recipeService, times(1)).findRecipeCommandByID(anyString());

    }

    @Test
    void getIngredientByIDForShowMVC() throws Exception{

        IngredientCommand ingredientCommand = IngredientCommand.builder().id("2").build();
        when(ingredientService.findByRecipeIdAndIngredientId(anyString(),anyString())).thenReturn(Mono.just(ingredientCommand));

       // when(recipeService.findRecipeCommandByID(anyLong())).thenReturn(recipeCommand);

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        mockMvc.perform(get("/recipe/99/ingredients/2/show"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("ingredient"))
                .andExpect(MockMvcResultMatchers.view().name("recipe/ingredient/show"));
        verify(ingredientService, times(1)).findByRecipeIdAndIngredientId(anyString(),anyString());

    }

    @Test
    void getIngredientByIDForUpdateMVC() throws Exception{

        IngredientCommand ingredientCommand = IngredientCommand.builder().id("2").build();
        when(ingredientService.findByRecipeIdAndIngredientId(anyString(),anyString())).
                thenReturn(Mono.just(ingredientCommand));

        when(unitOfMeasureService.listUnitOfMeasures()).thenReturn(Flux.just(new UnitOfMeasureCommand()));

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        mockMvc.perform(get("/recipe/99/ingredients/2/update"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("ingredient"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("uoms"))
                .andExpect(MockMvcResultMatchers.view().name("recipe/ingredient/ingredientform"));
        verify(ingredientService, times(1)).findByRecipeIdAndIngredientId(anyString(),anyString());

    }


    @Test
    @Disabled
    void getUnknownIngredientByIDMVC() throws Exception{

        //remove so we get a 404
        recipeCommand.setIngredients(new ArrayList<IngredientCommand>());

        when(recipeService.findRecipeCommandByID(anyString())).thenReturn(recipeCommand);

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        mockMvc.perform(get("/recipe/99/ingredients/2/show"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
        verify(recipeService, times(1)).findRecipeCommandByID(anyString());

    }



}
