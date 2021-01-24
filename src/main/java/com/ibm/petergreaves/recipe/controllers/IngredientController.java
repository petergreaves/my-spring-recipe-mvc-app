package com.ibm.petergreaves.recipe.controllers;

import com.ibm.petergreaves.recipe.commands.IngredientCommand;
import com.ibm.petergreaves.recipe.commands.RecipeCommand;
import com.ibm.petergreaves.recipe.services.IngredientService;
import com.ibm.petergreaves.recipe.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import javax.websocket.server.PathParam;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Controller
@Slf4j
public class IngredientController {

    private IngredientService ingredientService;
    private RecipeService recipeService;

    public IngredientController(IngredientService ingredientService, RecipeService recipeService) {
        this.ingredientService = ingredientService;
        this.recipeService = recipeService;
    }

    @GetMapping
    @RequestMapping("/recipe/{recipeID}/ingredients")
    public String getIngredientsForRecipeID(Model model, @PathVariable  String recipeID){
        log.debug("Getting ingredients for recipe : " + recipeID);

         RecipeCommand recipe=recipeService.findRecipeCommandByID(Long.valueOf(recipeID));
         model.addAttribute("recipe", recipe);

        return "recipe/ingredient/list";

    }

    @GetMapping
    @RequestMapping("/recipe/{recipeID}/ingredients/{ingredientID}/show")
    public String getIngredientForRecipeAndIngredientID(Model model,@PathVariable Map<String, String> pathVarsMap){


        final String view = "recipe/ingredient/show";

        final Long recipeID = Long.parseLong(pathVarsMap.get("recipeID"));
        final Long ingredID = Long.parseLong(pathVarsMap.get("ingredientID"));

        log.debug("Getting ingredients for recipe id : " + recipeID + ", ingredient id : " + ingredID);
        IngredientCommand ingredientCommand =
                ingredientService.findByRecipeIdAndIngredientId(recipeID, ingredID);
        model.addAttribute("ingredient", ingredientCommand);

        return view;

    }

}
