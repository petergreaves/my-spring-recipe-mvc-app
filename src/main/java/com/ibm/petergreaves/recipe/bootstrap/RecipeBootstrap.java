package com.ibm.petergreaves.recipe.bootstrap;

import com.ibm.petergreaves.recipe.domain.*;
import com.ibm.petergreaves.recipe.repositories.CategoryRepository;
import com.ibm.petergreaves.recipe.repositories.RecipeRepository;
import com.ibm.petergreaves.recipe.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Slf4j
@Component
@Profile("default")
public class RecipeBootstrap implements ApplicationListener<ContextRefreshedEvent> {

private UnitOfMeasureRepository unitOfMeasureRepository;
private CategoryRepository categoryRepository;
private RecipeRepository recipeRepository;


    public RecipeBootstrap(UnitOfMeasureRepository unitOfMeasureRepository, CategoryRepository categoryRepository, RecipeRepository recipeRepository) {
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.categoryRepository = categoryRepository;
        this.recipeRepository = recipeRepository;
    }


    private void loadCategories(){
        Category cat1 = new Category();
        cat1.setDescription("American");
        categoryRepository.save(cat1);

        Category cat2 = new Category();
        cat2.setDescription("Italian");
        categoryRepository.save(cat2);

        Category cat3 = new Category();
        cat3.setDescription("Mexican");
        categoryRepository.save(cat3);

        Category cat4 = new Category();
        cat4.setDescription("Fast Food");
        categoryRepository.save(cat4);
    }

    private void loadUom(){
        UnitOfMeasure uom1 = new UnitOfMeasure();
        uom1.setDescription("Teaspoon");
        unitOfMeasureRepository.save(uom1);

        UnitOfMeasure uom2 = new UnitOfMeasure();
        uom2.setDescription("Tablespoon");
        unitOfMeasureRepository.save(uom2);

        UnitOfMeasure uom3 = new UnitOfMeasure();
        uom3.setDescription("Cup");
        unitOfMeasureRepository.save(uom3);

        UnitOfMeasure uom4 = new UnitOfMeasure();
        uom4.setDescription("Pinch");
        unitOfMeasureRepository.save(uom4);

        UnitOfMeasure uom5 = new UnitOfMeasure();
        uom5.setDescription("Ounce");
        unitOfMeasureRepository.save(uom5);

        UnitOfMeasure uom6 = new UnitOfMeasure();
        uom6.setDescription("Each");
        unitOfMeasureRepository.save(uom6);

        UnitOfMeasure uom7 = new UnitOfMeasure();
        uom7.setDescription("Pint");
        unitOfMeasureRepository.save(uom7);

        UnitOfMeasure uom8 = new UnitOfMeasure();
        uom8.setDescription("Dash");
        unitOfMeasureRepository.save(uom8);
    }


    public List<Recipe> getRecipes() {


        // OUMs

        Optional<UnitOfMeasure> eachUom = unitOfMeasureRepository.findByDescription("Each");
        if (!eachUom.isPresent()) {
            throw new RuntimeException("Each unit of measure not found");
        }

        Optional<UnitOfMeasure> cupUom = unitOfMeasureRepository.findByDescription("Cup");
        if (!cupUom.isPresent()) {
            throw new RuntimeException("Cup unit of measure not found");
        }


        Optional<UnitOfMeasure> ounceUom = unitOfMeasureRepository.findByDescription("Ounce");
        if (!ounceUom.isPresent()) {
            throw new RuntimeException("Ounce unit of measure not found");
        }


        Optional<UnitOfMeasure> tablespoonUom = unitOfMeasureRepository.findByDescription("Tablespoon");
        if (!tablespoonUom.isPresent()) {
            throw new RuntimeException("Tablespoon unit of measure not found");
        }

        Optional<UnitOfMeasure> teaspoonUom = unitOfMeasureRepository.findByDescription("Teaspoon");
        if (!teaspoonUom.isPresent()) {
            throw new RuntimeException("Teaspoon unit of measure not found");
        }


        Optional<Category> catAmerican = categoryRepository.findByDescription("American");
        if (!catAmerican.isPresent()) {
            throw new RuntimeException("American category not found");
        }

        Optional<Category> catMexican = categoryRepository.findByDescription("Mexican");
        if (!catMexican.isPresent()) {
            throw new RuntimeException("Mexican category not found");
        }


        List<Recipe> retval = new ArrayList<>();

        //  Recipe guacamole = new Recipe();

        Notes guacNotes = new Notes();
        guacNotes.setRecipeNotes("Here are the notes for the guacamole recipe\n\nLovely!!");

        Ingredient sugar = Ingredient.builder()
                .description("Sugar")
                .uom(cupUom.get())
                .quantity(new BigDecimal(0.5))
                .build();
        Ingredient eggs = Ingredient.builder()
                .description("Eggs")
                .quantity(new BigDecimal(2))
                .uom(cupUom.get())
                .build();
        Ingredient flour = Ingredient.builder()
                .description("Flour")
                .quantity(new BigDecimal(1))
                .uom(cupUom.get())
                .build();
        Ingredient avocado = Ingredient.builder()
                .description("Avocado")
                .quantity(new BigDecimal(1))
                .uom(eachUom.get())
                .build();
        Ingredient mozzarella = Ingredient.builder()
                .description("Mozzarella")
                .quantity(new BigDecimal(4))
                .uom(ounceUom.get())
                .build();

        Ingredient tomatoSauce = Ingredient.builder()
                .description("Tomato Sauce")
                .quantity(new BigDecimal(1))
                .uom(cupUom.get())
                .build();


        Set<Category> guacCats = new HashSet<>();
        guacCats.add(catMexican.get());
        guacCats.add(catAmerican.get());

        log.debug("Created recipes...");

        Set<Ingredient> guacIngreds = new HashSet<>();


        guacIngreds.add(avocado);
        guacIngreds.add(eggs);
        guacIngreds.add(flour);

        Recipe guacamole = Recipe.builder()
                .title("Best ever guacamole")
                .cookTime(60)
                .prepTime(25)
                .difficulty(Difficulty.MODERATE)
                .notes(guacNotes)
                .directions("How to make guacamole")
                .ingredients(guacIngreds)
                .url("http://an.address")
                .source("http://src.address")
                .servings(4)
                .description("Best ever guacamole")
                .categories(guacCats)
                .build();

       // guacamole.getIngredients().forEach(ing -> ing.setRecipe(guacamole));

        retval.add(guacamole);

        HashSet<Category> pizzaCats = new HashSet<>();
        catAmerican.ifPresent(pizzaCats::add);

        Set<Ingredient> pizzaIngredients = new HashSet<>();
        pizzaIngredients.add(mozzarella);
        pizzaIngredients.add(flour);
        pizzaIngredients.add(tomatoSauce);


        Recipe pizza = Recipe.builder()
                .title("Pizza")
                .directions("Make a pizza....")
                .cookTime(30)
                .prepTime(15)
                .difficulty(Difficulty.EASY)
                .notes(guacNotes)
                .url("http://an.address")
                .source("http://src.address")
                .ingredients(new HashSet<>())
                .servings(4)
                .description("Pizza")
                .categories(pizzaCats)
                .build();

        pizza.addIngredient(tomatoSauce);
        pizza.addIngredient(mozzarella);

        retval.add(pizza);

        return retval;

    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        loadCategories();
        loadUom();
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
