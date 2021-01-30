package com.ibm.petergreaves.recipe.controllers;

import com.ibm.petergreaves.recipe.commands.RecipeCommand;
import com.ibm.petergreaves.recipe.domain.Recipe;
import com.ibm.petergreaves.recipe.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
public class RecipeController {

    private RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {

        this.recipeService=recipeService;
    }


    @GetMapping(value={"/recipe/{id}/show"})
    public String getRecipeByID(Model model, @PathVariable String id){

       log.debug("Got request for recipe " + id);
       Recipe recipe=recipeService.getRecipeByID(Long.parseLong(id));
       model.addAttribute("recipe", recipe);

        return "recipe/show";
    }


    @GetMapping(value={"/recipe/{id}/delete"})
    public String deleteRecipeByID(@PathVariable String id){

        log.debug("Got request to delete recipe " + id);
        recipeService.deleteByID(Long.parseLong(id));


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



}
