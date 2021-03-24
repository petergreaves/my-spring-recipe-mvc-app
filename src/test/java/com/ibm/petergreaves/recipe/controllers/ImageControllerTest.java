package com.ibm.petergreaves.recipe.controllers;

import com.ibm.petergreaves.recipe.commands.RecipeCommand;
import com.ibm.petergreaves.recipe.exceptions.NotFoundException;
import com.ibm.petergreaves.recipe.services.ImageService;
import com.ibm.petergreaves.recipe.services.RecipeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


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

    MockMvc mockMvc;

    @BeforeEach
    public void openMocks() {
        closeable = MockitoAnnotations.openMocks(this);

         mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }


    @AfterEach
    public void releaseMocks() throws Exception {
        closeable.close();
    }

    @Test
    void getImageForm() throws Exception{

        //given
        RecipeCommand command = new RecipeCommand();
        command.setId("1");

        when(recipeService.findRecipeCommandByID(anyString())).thenReturn(command);

        //when
        mockMvc.perform(get("/recipe/1/imageform"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"));

        verify(recipeService, times(1)).findRecipeCommandByID(anyString());
    }
    @Test
    void getImageForm404MVC() throws Exception {


        when(recipeService.findRecipeCommandByID(anyString())).thenReturn(null);

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

        RecipeCommand command = RecipeCommand.builder().id("33").image(bytesObject).build();
        when(recipeService.findRecipeCommandByID(anyString())).thenReturn(command);

        MockHttpServletResponse resp=mockMvc.perform(get("/recipe/1/recipeimage"))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        assertEquals(resp.getContentType(), "image/jpeg");
        assertTrue(resp.getContentAsByteArray().length==bytesObject.length);
    }

    @Test
    void getImageFormMVC() throws Exception{

        when(recipeService.findRecipeCommandByID(anyString())).thenReturn(RecipeCommand.builder().id("1").build());

        mockMvc.perform(get("/recipe/1/imageform"))
                .andExpect(status().isOk())
                .andExpect(view().name("/recipe/imageuploadform"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("recipe"));
    }

    @Test
    void handleImagePost() throws Exception{


        MockMultipartFile multipartFile =
                new MockMultipartFile("imagefile", "testing.txt", "text/plain",
                        "Spring Framework Guru".getBytes());

        mockMvc.perform(multipart("/recipe/1/image").file(multipartFile))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/recipe/1/show"));

        verify(imageService, times(1)).saveImageFile(anyString(), any());


    }

    @Test
    public void getImageBadRecipeID() throws Exception {

        when(recipeService.getRecipeByID(anyString())).thenThrow(NotFoundException.class);
        mockMvc.perform(get("/recipe/8/recipeimage"))
                .andExpect(status().is4xxClientError());

    }
}
