package com.ibm.petergreaves.recipe.controllers;

import com.ibm.petergreaves.recipe.commands.RecipeCommand;
import com.ibm.petergreaves.recipe.domain.Recipe;
import com.ibm.petergreaves.recipe.exceptions.NotFoundException;
import com.ibm.petergreaves.recipe.exceptions.ParamFormatException;
import com.ibm.petergreaves.recipe.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
public class RecipeController {

    private RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {

        this.recipeService=recipeService;
    }


    @GetMapping(value={"/recipe/{id}/show"})
    public String getRecipeByID(Model model, @PathVariable String id){

        Long idToGet = 0L;
        try {

            idToGet = Long.parseLong(id);

        }
        catch(NumberFormatException nfe){

            throw new ParamFormatException("Error trying to parse recipe id with request param for id : " + id +  ", long expected.");
        }

       log.debug("Got request for recipe " + idToGet);
       Recipe recipe=recipeService.getRecipeByID(idToGet);
       model.addAttribute("recipe", recipe);

        return "recipe/show";
    }


    @GetMapping(value={"/recipe/{id}/delete"})
    public String deleteRecipeByID(@PathVariable String id){

        Long idToGet = 0L;
        try {

            idToGet = Long.parseLong(id);

        }
        catch(NumberFormatException nfe){

            throw new ParamFormatException("Error trying to parse recipe id with request param for id : " + id +  ", long expected.");
        }

        log.debug("Got request to delete recipe " + idToGet);
        recipeService.deleteByID(idToGet);


        return "redirect:/";
    }



    @GetMapping(value={"/recipe/{id}/update"})
    public String getUpdateViewForRecipe(Model model, @PathVariable String id){

        log.debug("Got request for recipe for update" + id);
        RecipeCommand recipeCommand=recipeService.findRecipeCommandByID(Long.parseLong(id));
        model.addAttribute("recipe", recipeCommand);

        return "recipe/recipeform";
    }


    @GetMapping("/recipe/new")
    public String newRecipe(Model model){

        model.addAttribute("recipe", new RecipeCommand());

        return "recipe/recipeform";

    }


    @PostMapping("/recipe")
    public String doSaveOrUpdate(@ModelAttribute RecipeCommand recipe){

        RecipeCommand saved=recipeService.saveRecipeCommand(recipe);
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
    @ExceptionHandler(ParamFormatException.class)
    public ModelAndView handleParamFormatEx(Exception  exception ){

        log.error("Got a 400 error");
        log.error(exception.getMessage());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("ex", exception);
        modelAndView.setViewName("400error");

        return modelAndView;
    }

}
