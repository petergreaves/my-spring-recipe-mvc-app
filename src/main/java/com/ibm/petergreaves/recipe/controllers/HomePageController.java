package com.ibm.petergreaves.recipe.controllers;

import com.ibm.petergreaves.recipe.domain.Category;
import com.ibm.petergreaves.recipe.domain.UnitOfMeasure;
import com.ibm.petergreaves.recipe.repositories.CategoryRepository;
import com.ibm.petergreaves.recipe.repositories.UnitOfMeasureRepository;
import com.ibm.petergreaves.recipe.services.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
public class HomePageController {

    private RecipeService recipeService;

    public HomePageController(RecipeService recipeService) {

        this.recipeService=recipeService;
    }

    @RequestMapping({"/", "/index", "index.html", ""})
    public String doHome(Model model){

        //Optional<Category> categoryOptional = categoryRepository.findByDescription("Italian");
        //Optional<UnitOfMeasure> uomOptional = unitOfMeasureRepository.findByDescription("Cup");

       // System.out.println("Cat id : " + categoryOptional.get().getId());
       // System.out.println("UOM id : " + uomOptional.get().getId());

        model.addAttribute("recipes", recipeService.getRecipes());

        return "index";
    }
}
