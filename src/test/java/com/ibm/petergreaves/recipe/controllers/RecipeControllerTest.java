package com.ibm.petergreaves.recipe.controllers;

import com.ibm.petergreaves.recipe.commands.RecipeCommand;
import com.ibm.petergreaves.recipe.domain.Recipe;
import com.ibm.petergreaves.recipe.exceptions.NotFoundException;
import com.ibm.petergreaves.recipe.services.RecipeService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RecipeControllerTest {


    @Mock
    RecipeService recipeService;

    @InjectMocks
    RecipeController controller;

    @Mock
    Model model;

    @Mock
    BindingResult bindingResult;


    private AutoCloseable closeable;

    MockMvc mockMvc;

    @BeforeEach
    public void openMocks() {
        closeable = MockitoAnnotations.openMocks(this);
        controller = new RecipeController(recipeService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();

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
    void testPostNewRecipeCommand(){

        //given

        Long id = 33L;
        RecipeCommand command = new RecipeCommand();
        command.setTitle("title");

        ArgumentCaptor<RecipeCommand> argumentCaptor = ArgumentCaptor.forClass(RecipeCommand.class);
        when(recipeService.saveRecipeCommand(any(RecipeCommand.class))).thenReturn(command);

        controller.doSaveOrUpdate(command, bindingResult, model);

        verify(recipeService, times(1)).saveRecipeCommand(any(RecipeCommand.class));


    }


    @Test
    void testToNewRecipeForm(){

        String result = controller.newRecipe(model);
        assertEquals(result, "recipe/recipeform");
        verify(model, times(1)).addAttribute(anyString(), any(RecipeCommand.class));

    }


    @Test
    void testHandleNewRecipePost() throws Exception{

        RecipeCommand postCommand = new RecipeCommand();
        postCommand.setId(333L);

        when(recipeService.saveRecipeCommand(any(RecipeCommand.class))).thenReturn(postCommand);


        mockMvc.perform(post("/recipe")
                .param("id", "333")
                .param("description", "adfgdfgdfgdfg")
                .param("directions", "ahashjdahsdhasj")
                .param("cookTime", "10")
                .param("prepTime", "20")
                .param("url", "http://foo.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/333/show"));

    }

    @Test
    void testViewForNewRecipe() throws Exception{


        mockMvc.perform(get("/recipe/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/recipeform"));

        verify(recipeService, times(0)).getRecipeByID(anyLong());

    }
    @Test
    void testViewForShowRecipe() throws Exception{

        when(recipeService.getRecipeByID(33L)).thenReturn(any(Recipe.class));

        mockMvc.perform(get("/recipe/33/show"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/show"));

        verify(recipeService, times(1)).getRecipeByID(anyLong());

    }

    @Test
    void testViewForUpdateRecipe() throws Exception{

        RecipeCommand command = new RecipeCommand();
        command.setId(333L);

        when(recipeService.findRecipeCommandByID(333L)).thenReturn(command);

        mockMvc.perform(get("/recipe/333/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/recipeform"))
                .andExpect(model().attributeExists("recipe"));

        verify(recipeService, times(1)).findRecipeCommandByID(anyLong());


    }


    @Test
    void testDeleteAction(){

        //given
        Long idToDelete = 33L;
        controller.deleteRecipeByID(idToDelete+"");

        verify(recipeService, times(1)).deleteByID(anyLong());
    }

    @Test
    void testDeleteRequest() throws Exception{

        mockMvc.perform(get("/recipe/333/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"))
                .andExpect(model().attributeDoesNotExist("recipe"));

        verify(recipeService, times(1)).deleteByID(anyLong());

    }

    @Test
    public void notFoundRecipe() throws Exception{

        when(recipeService.getRecipeByID(anyLong())).thenThrow(NotFoundException.class);


        mockMvc.perform(get("/recipe/5/show"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("404error"));
    }

    @Test
    public void handleBadRecipeParam() throws Exception{


        mockMvc.perform(get("/recipe/a/show"))
                .andExpect(status().is4xxClientError())
                .andExpect(view().name("400error"));
    }

    @Test
    public void validateNewGoodRecipe() throws Exception {

        when(recipeService.saveRecipeCommand(any(RecipeCommand.class))).thenReturn(RecipeCommand.builder().id(55L).build());
        mockMvc.perform(post("/recipe")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "55")
                .param("description", "adfgdfgdfgdfg")
                .param("directions", "ahashjdahsdhasj")
                .param("cookTime", "10")
                .param("prepTime", "20")
                .param("url", "http://foo.com"))
                .andExpect(model().hasNoErrors())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/55/show"));

    }

    @Test
    public void validateBadRecipeNoDirections() throws Exception {

        when(recipeService.saveRecipeCommand(any(RecipeCommand.class))).thenReturn(RecipeCommand.builder().id(55L).build());
        mockMvc.perform(post("/recipe")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "55")
                .param("description", "adfgdfgdfgdfg")
                .param("cookTime", "10")
                .param("prepTime", "20")
                .param("url", "http://foo.com"))
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(view().name("recipe/recipeform"));

    }

}
