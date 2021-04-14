package com.ibm.petergreaves.recipe.controllers;

import com.ibm.petergreaves.recipe.commands.RecipeCommand;
import com.ibm.petergreaves.recipe.domain.Recipe;
import com.ibm.petergreaves.recipe.exceptions.NotFoundException;
import com.ibm.petergreaves.recipe.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.exceptions.TemplateInputException;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@Controller
public class RecipeController {

    private final RecipeService recipeService;
    private static final String RECIPE_FORM_VIEWNAME = "recipe/recipeform";

    private String savedRecipeID = null;

    private WebDataBinder webDataBinder;

    public RecipeController(RecipeService recipeService) {

        this.recipeService = recipeService;
    }

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {

        this.webDataBinder = webDataBinder;
    }


    @GetMapping(value = {"/recipe/{id}/show"})
    public String getRecipeByID(Model model, @PathVariable String id) {

        log.debug("Got request for recipe " + id);
        Mono<Recipe> recipe = recipeService.getRecipeByID(id);
        model.addAttribute("recipe", recipe);

        return "recipe/show";
    }


    @GetMapping(value = {"/recipe/{id}/delete"})
    public String deleteRecipeByID(@PathVariable String id) {


        log.debug("Got request to delete recipe " + id);
        recipeService.deleteByID(id);


        return "redirect:/";
    }


    @GetMapping(value = {"/recipe/{id}/update"})
    public String getUpdateViewForRecipe(Model model, @PathVariable String id) {

        log.debug("Got request for recipe for update with id : " + id);
        Mono<RecipeCommand> recipeCommand = recipeService.findRecipeCommandByID(id);
        model.addAttribute("recipe", recipeCommand);

        return RECIPE_FORM_VIEWNAME;
    }


    @GetMapping("/recipe/new")
    public String newRecipe(Model model) {

        model.addAttribute("recipe", new RecipeCommand());

        return RECIPE_FORM_VIEWNAME;

    }


    @PostMapping("/recipe")
    public String doSaveOrUpdate(@ModelAttribute("recipe") RecipeCommand recipe) {

        webDataBinder.validate();
        BindingResult bindingResult = webDataBinder.getBindingResult();

        if (bindingResult.hasErrors()) {

            for (ObjectError allError : bindingResult.getAllErrors()) {
                log.error("Recipe error validating : " + allError.getDefaultMessage());
            }
            return RECIPE_FORM_VIEWNAME;

        }


        // save a new or existing recipe

        String newRecipeID = UUID.randomUUID().toString();
        if (recipe.getId() == null) {
            recipe.setId(newRecipeID);
        }
        recipeService.saveRecipeCommand(recipe).subscribe();
        //    recipeService.saveRecipeCommand(recipe).subscribe(savedRecipe ->{

        //        log.info("saved " + savedRecipe.getDescription());
        //    });
        return "redirect:/recipe/" + recipe.getId() + "/show";
    }


    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({NotFoundException.class, TemplateInputException.class})
    public String handleNoSuchRecipe(Exception exception, Model model) {

        log.error("Got a 404 error");
        log.error(exception.getMessage());
        model.addAttribute("ex", exception);
        return "404error";

    }
}
