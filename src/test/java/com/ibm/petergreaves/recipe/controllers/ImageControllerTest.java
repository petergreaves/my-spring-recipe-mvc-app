package com.ibm.petergreaves.recipe.controllers;

import com.ibm.petergreaves.recipe.commands.IngredientCommand;
import com.ibm.petergreaves.recipe.commands.RecipeCommand;
import com.ibm.petergreaves.recipe.services.ImageService;
import com.ibm.petergreaves.recipe.services.IngredientService;
import com.ibm.petergreaves.recipe.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Controller;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class ImageControllerTest {

    @Mock
    ImageService imageService;

    @Mock
    RecipeService recipeService;

    @Mock
    Model model;

    @InjectMocks
    ImageController controller;

    private AutoCloseable closeable;

    @BeforeEach
    public void openMocks() {
        closeable = MockitoAnnotations.openMocks(this);
    }


    @AfterEach
    public void releaseMocks() throws Exception {
        closeable.close();
    }

    @Test
    void getImageForm(){

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        when(recipeService.findRecipeCommandByID(anyLong())).thenReturn(RecipeCommand.builder().id(1L).build());
        Long recipeID = 1L;
        String location=controller.getImageForm(model, recipeID+"");
        verify(model, times(1)).addAttribute(eq("recipe"), argumentCaptor.capture());
        verify(recipeService, times(1)).findRecipeCommandByID(anyLong());
        verify(imageService, times(0)).saveImageFile(anyLong(), any());

    }
    @Test
    void getImageForm404MVC() throws Exception {

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        when(recipeService.findRecipeCommandByID(anyLong())).thenReturn(null);

        mockMvc.perform(get("/recipe/1/imageform"))
                .andExpect(status().is4xxClientError());

    }


    @Test
    void renderImageFromDB() throws Exception {

        byte[] bytesPrimitive = new String("hello world").getBytes(StandardCharsets.UTF_8);

        Byte[] bytesObject = new Byte[bytesPrimitive.length];

        int k = 0;
        for (byte b : bytesPrimitive){

            bytesObject[k++] = b;
        }

        RecipeCommand command = RecipeCommand.builder().id(33L).image(bytesObject).build();
        when(recipeService.findRecipeCommandByID(anyLong())).thenReturn(command);

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        MockHttpServletResponse resp=mockMvc.perform(get("/recipe/1/recipeimage"))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        assertTrue(resp.getContentType().equals("image/jpeg"));
        assertTrue(resp.getContentAsByteArray().length==bytesObject.length);
    }

    @Test
    void getImageFormMVC() throws Exception{

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        when(recipeService.findRecipeCommandByID(anyLong())).thenReturn(RecipeCommand.builder().id(1L).build());

        mockMvc.perform(get("/recipe/1/imageform"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/recipe/imageuploadform"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("recipe"));
    }

    @Test
    void handleImagePost() throws Exception{

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        MockMultipartFile multipartFile =
                new MockMultipartFile("imagefile", "testing.txt", "text/plain",
                        "Spring Framework Guru".getBytes());

        mockMvc.perform(multipart("/recipe/1/image").file(multipartFile))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/recipe/1/show"));

        verify(imageService, times(1)).saveImageFile(anyLong(), any());


    }




}
