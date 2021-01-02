package com.ibm.petergreaves.recipe.bootstrap;

import com.ibm.petergreaves.recipe.domain.*;
import com.ibm.petergreaves.recipe.repositories.CategoryRepository;
import com.ibm.petergreaves.recipe.repositories.RecipeRepository;
import com.ibm.petergreaves.recipe.repositories.UnitOfMeasureRepository;
import com.ibm.petergreaves.recipe.utils.RecipeBuilder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

@Component
public class RecipeBootstrap implements ApplicationListener<ContextRefreshedEvent> {

private UnitOfMeasureRepository uomRepository;
private CategoryRepository categoryRepository;
private RecipeRepository recipeRepository;

    public RecipeBootstrap(UnitOfMeasureRepository uomRepository, CategoryRepository categoryRepository, RecipeRepository recipeRepository) {
        this.uomRepository = uomRepository;
        this.categoryRepository = categoryRepository;
        this.recipeRepository = recipeRepository;
    }


    public List<Recipe> getRecipes()  {


        // OUMs

        Optional<UnitOfMeasure> eachUom = uomRepository.findByDescription("Each");
        if (!eachUom.isPresent()){
            throw new RuntimeException("Each unit of measure not found");
        }

        Optional<UnitOfMeasure> cupUom = uomRepository.findByDescription("Cup");
        if (!cupUom.isPresent()){
            throw new RuntimeException("Cup unit of measure not found");
        }


        Optional<UnitOfMeasure> ounceUom = uomRepository.findByDescription("Ounce");
        if (!ounceUom.isPresent()){
            throw new RuntimeException("Ounce unit of measure not found");
        }


        Optional<UnitOfMeasure> tablespoonUom = uomRepository.findByDescription("Tablespoon");
        if (!tablespoonUom.isPresent()){
            throw new RuntimeException("Tablespoon unit of measure not found");
        }

        Optional<UnitOfMeasure> teaspoonUom = uomRepository.findByDescription("Teaspoon");
        if (!teaspoonUom.isPresent()){
            throw new RuntimeException("Teaspoon unit of measure not found");
        }


        Optional<Category> catAmerican = categoryRepository.findByDescription("American");
        if (!catAmerican.isPresent()){
            throw new RuntimeException("American category not found");
        }

        Optional<Category> catMexican = categoryRepository.findByDescription("Mexican");
        if (!catMexican.isPresent()){
            throw new RuntimeException("Mexican category not found");
        }



        List<Recipe> retval = new ArrayList<>();

        Recipe guacamole = new Recipe();

        Notes guacNotes = new Notes();
        guacNotes.setRecipeNotes("here are the notes for the guacamole recipe\n\nLovely!!");


        Ingredient eggs = new Ingredient("Eggs",new BigDecimal(2), eachUom.get());
        Ingredient flour = new Ingredient("Flour",new BigDecimal(0.5), cupUom.get());
        Ingredient avocado = new Ingredient("Ripe avocado",new BigDecimal(1), eachUom.get());



        guacamole.setTitle("Best ever guacamole");
        guacamole.setCookTime(60);
        guacamole.setPrepTime(25);
        guacamole.setDifficulty(Difficulty.MODERATE);
        guacamole.setNotes(guacNotes);
        guacamole.setUrl("http://an.address");
        guacamole.setSource("http://src.address'");
        guacamole.addIngredient(flour);
        guacamole.addIngredient(eggs);
        guacamole.addIngredient(avocado);

      //  System.out.println(guacamole.getIngredients());

        guacamole.setServings(4);
        guacamole.setDescription("this is the best ever guacamole");
        guacamole.getCategories().add(catAmerican.get());
        guacamole.getCategories().add(catMexican.get());

        retval.add(guacamole);
        return retval;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        recipeRepository.saveAll(getRecipes());

    }


    //   i.set

        /*
        2"ripe avocados
        1/4 teaspoon of salt, more to taste
        1 tablespoon fresh lime juice or lemon juice
        2 tablespoons to 1/4 cup of minced red onion or thinly sliced green onion
        1-2 serrano chiles, stems and seeds removed, minced
        2 tablespoons cilantro (leaves and tender stems), finely chopped
        A dash of freshly grated black pepper
        1/2 ripe tomato, seeds and pulp removed, chopped
        Red radishes or jicama, to garnish
        Tortilla chips, to serve
        */



}
