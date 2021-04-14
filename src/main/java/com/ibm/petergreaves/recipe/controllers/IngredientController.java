package com.ibm.petergreaves.recipe.controllers;

import com.ibm.petergreaves.recipe.commands.IngredientCommand;
import com.ibm.petergreaves.recipe.commands.RecipeCommand;
import com.ibm.petergreaves.recipe.commands.UnitOfMeasureCommand;
import com.ibm.petergreaves.recipe.domain.UnitOfMeasure;
import com.ibm.petergreaves.recipe.services.IngredientService;
import com.ibm.petergreaves.recipe.services.RecipeService;
import com.ibm.petergreaves.recipe.services.UnitOfMeasureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class IngredientController {


    private static final String VIEW_INGREDIENT_FORM = "recipe/ingredient/ingredientform";
    private static final String VIEW_INGREDIENT_LIST = "recipe/ingredient/list";


    private IngredientService ingredientService;
    private RecipeService recipeService;
    private UnitOfMeasureService unitOfMeasureService;

    private WebDataBinder webDataBinder;

    public IngredientController(IngredientService ingredientService, RecipeService recipeService, UnitOfMeasureService unitOfMeasureService) {
        this.ingredientService = ingredientService;
        this.recipeService = recipeService;
        this.unitOfMeasureService = unitOfMeasureService;
    }

    @InitBinder("ingredient")
    public void initBinder(WebDataBinder binder){

        this.webDataBinder = binder;
    }

    @GetMapping("/recipe/{recipeID}/ingredients")
    public String getIngredientsForRecipeID(Model model, @PathVariable String recipeID) {
        log.debug("Getting ingredients for recipe : " + recipeID);

        Mono<RecipeCommand> recipe = recipeService.findRecipeCommandByID(recipeID);
        model.addAttribute("recipe", recipe);

        return VIEW_INGREDIENT_LIST;

    }


    @GetMapping("/recipe/{recipeID}/ingredients/{ingredientID}/show")
    public String getIngredientForRecipeAndIngredientIDShow(Model model, @PathVariable Map<String, String> pathVarsMap) {

        final String recipeID = pathVarsMap.get("recipeID");
        final String ingredID = pathVarsMap.get("ingredientID");

        log.debug("Getting ingredient for recipe id : " + recipeID + ", ingredient id : " + ingredID + " for show");
        Mono<IngredientCommand> ingredientCommand =
                ingredientService.findByRecipeIdAndIngredientId(recipeID, ingredID);
        model.addAttribute("ingredient", ingredientCommand);

        return "/recipe/ingredient/show";

    }

    @GetMapping("/recipe/{recipeID}/ingredients/{ingredientID}/update")
    public String getIngredientForRecipeAndIngredientIDUpdate(Model model, @PathVariable Map<String, String> pathVarsMap) {


        final String recipeID = pathVarsMap.get("recipeID");
        final String ingredID = pathVarsMap.get("ingredientID");

        log.debug("Getting ingredient for recipe id : " + recipeID + ", ingredient id : " + ingredID + " for update");
        Mono<IngredientCommand> ingredientCommandMono =
                ingredientService.findByRecipeIdAndIngredientId(recipeID, ingredID);

        model.addAttribute("ingredient", ingredientCommandMono);

        return VIEW_INGREDIENT_FORM;

    }


    @PostMapping("/recipe/{recipeID}/ingredient")
    public String doSaveOrUpdateIngredient(@ModelAttribute("ingredient") IngredientCommand ingredientCommand, @PathVariable String recipeID, Model model) {

        webDataBinder.validate();
        BindingResult bindingResult = webDataBinder.getBindingResult();

            if (bindingResult.hasErrors()) {
                for (ObjectError allError : bindingResult.getAllErrors()) {
                    log.error("Ingredient error validating : " + allError.getDefaultMessage());
                }
                return VIEW_INGREDIENT_FORM;

            }

        log.debug("Got an update to ingredients for recipe id : " + recipeID);
        ingredientService.saveIngredientCommand(ingredientCommand).subscribe(savedIngr -> {
            log.debug("Returning ingred command : " + savedIngr);
        });
        return "redirect:/recipe/" + recipeID + "/ingredients";
    }


    @GetMapping("/recipe/{recipeID}/ingredients/{ingredientID}/delete")
    public String doDeleteIngredient(Model model, @PathVariable Map<String, String> paramMap) {

        String recipeID = paramMap.get("recipeID");
        String ingredientID = paramMap.get("ingredientID");

        log.debug("Got request to remove ingredient id : " + ingredientID + " from recipe id : " + recipeID);

        // get an ingredientcommand

        Mono<IngredientCommand> commandToRemove = ingredientService.findByRecipeIdAndIngredientId(recipeID, ingredientID);
        // TODO 404 if we can't find either

        commandToRemove.subscribe(ic -> ingredientService.removeIngredientCommand(ic).subscribe());

        Mono<RecipeCommand> recipe = recipeService.findRecipeCommandByID(recipeID);
        model.addAttribute("recipe", recipe);


        return "redirect:/recipe/" + recipeID + "/ingredients";
    }


    @GetMapping("/recipe/{recipeID}/ingredient/new")
    public String newRecipeIngredient(Model model, @PathVariable String recipeID) {

        Mono<RecipeCommand> recipeCommand = recipeService.findRecipeCommandByID(recipeID);
        // assume for now we have a recipe, but we should execptn if null

        IngredientCommand ingredientCommand = IngredientCommand.builder().build();
        ingredientCommand.setUom(new UnitOfMeasureCommand());

        ingredientCommand.setRecipeID(recipeID);

        model.addAttribute("ingredient", ingredientCommand);

        return VIEW_INGREDIENT_FORM;
    }

    @ModelAttribute("uoms")
    public Flux<UnitOfMeasureCommand> populateUOMs(){
        return unitOfMeasureService.listUnitOfMeasures();

    }

}
