package com.ibm.petergreaves.recipe.services;

import com.ibm.petergreaves.recipe.converters.IngredientCommandToIngredient;
import com.ibm.petergreaves.recipe.converters.IngredientToIngredientCommand;
import com.ibm.petergreaves.recipe.domain.Recipe;
import com.ibm.petergreaves.recipe.repositories.RecipeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import static org.junit.jupiter.api.Assertions.*;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class ImageServiceImplTest {

    @Mock
    RecipeRepository recipeRepository;

    @Mock
    MultipartFile file;

    @InjectMocks
    ImageServiceImpl service;

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
    void testSaveImageFile() throws Exception{


        MultipartFile multipartFile = new MockMultipartFile("imagefile", "testing.txt", "text/plain",
                "Spring Framework Guru".getBytes());
        Recipe recipe = Recipe.builder().id(33L).build();

        when(recipeRepository.findById(anyLong())).thenReturn(Optional.of(recipe));

        ArgumentCaptor<Recipe> captor = ArgumentCaptor.forClass(Recipe.class);

        service.saveImageFile(33L, multipartFile);
        verify(recipeRepository, times(1)).save(captor.capture());

        Recipe savedRecipe = captor.getValue();

        assertEquals(multipartFile.getBytes().length,recipe.getImage().length);


    }
}
