package com.ibm.petergreaves.recipe.controllers;

import com.ibm.petergreaves.recipe.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
public class HomePageController {

    private RecipeService recipeService;

    public HomePageController(RecipeService recipeService) {

        this.recipeService=recipeService;
    }

    @RequestMapping(path={"/", "/index", "index.html", ""})
    public String doHome(Model model){

        //Optional<Category> categoryOptional = categoryRepository.findByDescription("Italian");
        //Optional<UnitOfMeasure> uomOptional = unitOfMeasureRepository.findByDescription("Cup");

       // System.out.println("Cat id : " + categoryOptional.get().getId());
       // System.out.println("UOM id : " + uomOptional.get().getId());
        log.debug("Got request for home page");
        model.addAttribute("recipes", recipeService.getRecipes());

        return "index";
    }
}
