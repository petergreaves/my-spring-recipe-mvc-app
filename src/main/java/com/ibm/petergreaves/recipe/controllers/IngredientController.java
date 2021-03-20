package com.ibm.petergreaves.recipe.controllers;

import com.ibm.petergreaves.recipe.commands.IngredientCommand;
import com.ibm.petergreaves.recipe.commands.RecipeCommand;
import com.ibm.petergreaves.recipe.commands.UnitOfMeasureCommand;
import com.ibm.petergreaves.recipe.services.IngredientService;
import com.ibm.petergreaves.recipe.services.RecipeService;
import com.ibm.petergreaves.recipe.services.UnitOfMeasureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@Slf4j
public class IngredientController {

    private IngredientService ingredientService;
    private RecipeService recipeService;
    private UnitOfMeasureService unitOfMeasureService;

    public IngredientController(IngredientService ingredientService, RecipeService recipeService, UnitOfMeasureService unitOfMeasureService) {
        this.ingredientService = ingredientService;
        this.recipeService = recipeService;
        this.unitOfMeasureService = unitOfMeasureService;
    }

    @GetMapping("/recipe/{recipeID}/ingredients")
    public String getIngredientsForRecipeID(Model model, @PathVariable String recipeID) {
        log.debug("Getting ingredients for recipe : " + recipeID);

        RecipeCommand recipe = recipeService.findRecipeCommandByID(recipeID);
        model.addAttribute("recipe", recipe);

        return "recipe/ingredient/list";

    }


    @GetMapping("/recipe/{recipeID}/ingredients/{ingredientID}/show")
    public String getIngredientForRecipeAndIngredientIDShow(Model model, @PathVariable Map<String, String> pathVarsMap) {


        final String view = "recipe/ingredient/show";

        final String recipeID = pathVarsMap.get("recipeID");
        final String ingredID = pathVarsMap.get("ingredientID");

        log.debug("Getting ingredient for recipe id : " + recipeID + ", ingredient id : " + ingredID + " for show");
        IngredientCommand ingredientCommand =
                ingredientService.findByRecipeIdAndIngredientId(recipeID, ingredID);
        model.addAttribute("ingredient", ingredientCommand);

        return view;

    }


    @GetMapping("/recipe/{recipeID}/ingredients/{ingredientID}/update")
    public String getIngredientForRecipeAndIngredientIDUpdate(Model model, @PathVariable Map<String, String> pathVarsMap) {


        final String view = "recipe/ingredient/ingredientform";

        final String recipeID = pathVarsMap.get("recipeID");
        final String ingredID = pathVarsMap.get("ingredientID");

        log.debug("Getting ingredient for recipe id : " + recipeID + ", ingredient id : " + ingredID + " for update");
        IngredientCommand ingredientCommand =
                ingredientService.findByRecipeIdAndIngredientId(recipeID, ingredID);
  //      ingredientCommand.setRecipeID(recipeID);
        model.addAttribute("ingredient", ingredientCommand);
        model.addAttribute("uoms", unitOfMeasureService.listUnitOfMeasures());

        return view;

    }


    @PostMapping("/recipe/{recipeID}/ingredient")
    public String doSaveOrUpdateIngredient(@ModelAttribute IngredientCommand ingredientCommand, @PathVariable String recipeID) {

        log.debug("Got an update to ingredients for recipe id : " + recipeID);
        IngredientCommand saved = ingredientService.saveIngredientCommand(ingredientCommand);
        log.debug("Returning ingred command : " + saved);
        return "redirect:/recipe/" + recipeID + "/ingredients";
    }


    @GetMapping("/recipe/{recipeID}/ingredients/{ingredientID}/delete")
    public String doDeleteIngredient(Model model, @PathVariable Map<String, String> paramMap) {

        String recipeID = paramMap.get("recipeID");
        String ingredientID = paramMap.get("ingredientID");

        log.debug("Got request to remove ingredient id : " + ingredientID + " from recipe id : " + recipeID);

        // get an ingredientcommand

        IngredientCommand commandToRemove = ingredientService.findByRecipeIdAndIngredientId(recipeID, ingredientID);
        // TODO 404 if we can't find either

        ingredientService.removeIngredientCommand(commandToRemove);

        RecipeCommand recipe = recipeService.findRecipeCommandByID(recipeID);
        model.addAttribute("recipe", recipe);


        return "redirect:/recipe/" + recipeID + "/ingredients";
    }



    @GetMapping("/recipe/{recipeID}/ingredient/new")
    public String newRecipeIngredient(Model model, @PathVariable String recipeID) {

        RecipeCommand recipeCommand = recipeService.findRecipeCommandByID(recipeID);
        // assume for now we have a recipe, but we should execptn if null

        IngredientCommand ingredientCommand = IngredientCommand.builder().build();
        ingredientCommand.setUom(new UnitOfMeasureCommand());

        ingredientCommand.setRecipeID(recipeCommand.getId());

        model.addAttribute("ingredient", ingredientCommand);

        // need to add the list for the form to display
        model.addAttribute("uoms", unitOfMeasureService.listUnitOfMeasures());

        return "recipe/ingredient/ingredientform";
    }


}
