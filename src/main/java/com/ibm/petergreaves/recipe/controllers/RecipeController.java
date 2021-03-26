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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
public class RecipeController {

    private final RecipeService recipeService;
    private static final String RECIPE_FORM_VIEWNAME = "recipe/recipeform";

    public RecipeController(RecipeService recipeService) {

        this.recipeService=recipeService;
    }


    @GetMapping(value={"/recipe/{id}/show"})
    public String getRecipeByID(Model model, @PathVariable String id){

       log.debug("Got request for recipe " + id);
       Recipe recipe=recipeService.getRecipeByID(id).block();
       model.addAttribute("recipe", recipe);

        return "recipe/show";
    }


    @GetMapping(value={"/recipe/{id}/delete"})
    public String deleteRecipeByID(@PathVariable String id){


        log.debug("Got request to delete recipe " + id);
        recipeService.deleteByID(id);


        return "redirect:/";
    }



    @GetMapping(value={"/recipe/{id}/update"})
    public String getUpdateViewForRecipe(Model model, @PathVariable String id){

        log.debug("Got request for recipe for update with id : " + id);
        RecipeCommand recipeCommand=recipeService.findRecipeCommandByID(id).block();
        model.addAttribute("recipe", recipeCommand);

        return RECIPE_FORM_VIEWNAME;
    }


    @GetMapping("/recipe/new")
    public String newRecipe(Model model){

        model.addAttribute("recipe", new RecipeCommand());

        return RECIPE_FORM_VIEWNAME;

    }


    @PostMapping("/recipe")
    public String doSaveOrUpdate(@Validated @ModelAttribute("recipe") RecipeCommand recipe, BindingResult bindingResult, Model model){

        if (bindingResult.hasErrors()){

            for (ObjectError allError : bindingResult.getAllErrors()) {
                    log.error("Recipe error validating : " +allError.getDefaultMessage());
            }
            return RECIPE_FORM_VIEWNAME;

        }

        RecipeCommand saved=recipeService.saveRecipeCommand(recipe).block();
        return "redirect:/recipe/"+saved.getId()+"/show";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ModelAndView handleNoSuchRecipe(Exception  exception ){

        log.error("Got a 404 error");
        log.error(exception.getMessage());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("ex", exception);
        modelAndView.setViewName("404error");

        return modelAndView;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NumberFormatException.class)
    public ModelAndView handleParamFormatEx(Exception  exception ){

        log.error("Got a 400 error");
        log.error(exception.getMessage());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("ex", exception);
        modelAndView.setViewName("400error");

        return modelAndView;
    }

}
