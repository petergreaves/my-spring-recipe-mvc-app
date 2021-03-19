package com.ibm.petergreaves.recipe.services;

import com.ibm.petergreaves.recipe.domain.Recipe;
import com.ibm.petergreaves.recipe.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Service
@Slf4j
public class ImageServiceImpl implements ImageService{

    private RecipeRepository recipeRepository;

    @Override
    public void saveImageFile(String recipeId, MultipartFile file) {

        log.debug("Got an image file for " + recipeId);

        Optional<Recipe> recipeOptional =recipeRepository.findById(recipeId);

        if (recipeOptional.isPresent()){
            Recipe recipe = recipeOptional.get();
            try{
                byte[] bytesPrim = file.getBytes();
                Byte[] bytes = new Byte[bytesPrim.length];
                Arrays.setAll(bytes, n -> bytesPrim[n]);
                recipe.setImage(bytes);
                recipeRepository.save(recipe);
                log.debug("Saved image for recipe " + recipeId);
            }
            catch (IOException e) {

                // TODO better error handling
                e.printStackTrace();
                log.error("Couldn't get image bytes");
            }

        }
    }

    public ImageServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;


    }
}
