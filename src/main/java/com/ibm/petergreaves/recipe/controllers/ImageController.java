package com.ibm.petergreaves.recipe.controllers;

import com.ibm.petergreaves.recipe.commands.RecipeCommand;
import com.ibm.petergreaves.recipe.converters.RecipeToRecipeCommand;
import com.ibm.petergreaves.recipe.domain.Recipe;
import com.ibm.petergreaves.recipe.exceptions.NotFoundException;
import com.ibm.petergreaves.recipe.services.ImageService;
import com.ibm.petergreaves.recipe.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

@Controller
@Slf4j
public class ImageController {

    private ImageService service;
    private RecipeService recipeService;
    private RecipeToRecipeCommand recipeToRecipeCommand;

    public ImageController(ImageService service, RecipeService recipeService) {
        this.service = service;
        this.recipeService = recipeService;
        this.recipeToRecipeCommand = new RecipeToRecipeCommand();
    }

    @GetMapping("/recipe/{recipeID}/imageform")
    public String getImageForm(Model model, @PathVariable String recipeID) {


        RecipeCommand command = recipeService.findRecipeCommandByID(recipeID).block();
        if (command == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Recipe not found with id " + recipeID
            );
        }
        model.addAttribute("recipe", command);

        return "/recipe/imageuploadform";

    }

    @GetMapping("/recipe/{recipeID}/recipeimage")
    public void getImageStream(@PathVariable String recipeID, HttpServletResponse response) throws IOException {

        RecipeCommand command = recipeService.findRecipeCommandByID(recipeID).block();
        if (command==null){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Recipe not found with id " +recipeID
            );
        }

        Byte[] imageBytesObj = command.getImage();
        if (imageBytesObj != null & imageBytesObj.length > 0) {
            byte[] imageBytesPrim = new byte[imageBytesObj.length];

            int k = 0;

            for (byte imageByte : imageBytesObj) {
                imageBytesPrim[k++] = imageByte;
            }

            response.setContentType("image/jpeg");
            InputStream is = new ByteArrayInputStream(imageBytesPrim);
            IOUtils.copy(is, response.getOutputStream());
        }
    }

    @PostMapping("recipe/{id}/image")
    public String handleImagePost(@PathVariable String id, @RequestParam("imagefile") MultipartFile file) {

        service.saveImageFile(id, file);

        return "redirect:/recipe/" + id + "/show";
    }
}
