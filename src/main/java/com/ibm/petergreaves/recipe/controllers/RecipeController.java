package com.ibm.petergreaves.recipe.controllers;

import com.ibm.petergreaves.recipe.domain.Recipe;
import com.ibm.petergreaves.recipe.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Slf4j
@Controller
public class RecipeController {

    private RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {

        this.recipeService=recipeService;
    }

    @RequestMapping(value={"/recipe/{id}/show"}, method = RequestMethod.GET)
    public String getRecipeByID(Model model, @PathVariable String id){

       log.debug("Got request for recipe " + id);
       Recipe recipe=recipeService.getRecipeByID(Long.parseLong(id));
       model.addAttribute("recipe", recipe);

        return "recipe/show";
    }


}
