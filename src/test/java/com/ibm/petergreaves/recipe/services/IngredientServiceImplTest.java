package com.ibm.petergreaves.recipe.services;

import com.ibm.petergreaves.recipe.commands.IngredientCommand;
import com.ibm.petergreaves.recipe.commands.RecipeCommand;
import com.ibm.petergreaves.recipe.converters.IngredientCommandToIngredient;
import com.ibm.petergreaves.recipe.converters.IngredientToIngredientCommand;
import com.ibm.petergreaves.recipe.converters.RecipeCommandToRecipe;
import com.ibm.petergreaves.recipe.converters.RecipeToRecipeCommand;
import com.ibm.petergreaves.recipe.domain.Ingredient;
import com.ibm.petergreaves.recipe.domain.Recipe;
import com.ibm.petergreaves.recipe.domain.UnitOfMeasure;
import com.ibm.petergreaves.recipe.repositories.RecipeRepository;
import com.ibm.petergreaves.recipe.repositories.UnitOfMeasureRepository;
import com.ibm.petergreaves.recipe.repositories.reactive.RecipeReactiveRepository;
import com.ibm.petergreaves.recipe.repositories.reactive.UnitOfMeasureReactiveRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class IngredientServiceImplTest {

    IngredientServiceImpl ingredientService;

    @Mock
    RecipeReactiveRepository recipeReactiveRepository;

    @Mock
    UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;

    private AutoCloseable closeable;

    @BeforeEach
    public void openMocks() {
        closeable = MockitoAnnotations.openMocks(this);
        ingredientService = new IngredientServiceImpl(recipeReactiveRepository,
                new IngredientCommandToIngredient(),
                new IngredientToIngredientCommand(), unitOfMeasureReactiveRepository);
    }

    @AfterEach
    public void releaseMocks() throws Exception {
        closeable.close();
    }

    @Test
    void getIngredientForRecipeIDAndRecipeIDHappyPath() {

        Recipe recipe = new Recipe();

        recipe.setId("9");

        UnitOfMeasure uom = new UnitOfMeasure();
        uom.setDescription("spoonful");
        uom.setId("4");

        Ingredient i1 = new Ingredient();
        i1.setId("1");
             i1.setUom(uom);


        Ingredient i2 = new Ingredient();
        i2.setId("2");
               i2.setUom(uom);

        Ingredient i3 = new Ingredient();
        i3.setId("3");
            i3.setUom(uom);

        Set<Ingredient> ingredientSet = new HashSet<>();

        ingredientSet.add(i1);
        ingredientSet.add(i2);
        ingredientSet.add(i3);

        ingredientSet.forEach(i -> recipe.addIngredient(i));

        Optional<Recipe> recipeOptional = Optional.of(recipe);

        when(recipeReactiveRepository.findById(anyString())).thenReturn(Mono.just(recipe));

        IngredientCommand ic = ingredientService.findByRecipeIdAndIngredientId("9", "1").block();
        assertNotNull(ic);
        assertTrue(ic.getId().equals("1"));
        assertNotNull(ic.getUom().getDescription());
        assertEquals(ic.getRecipeID(), "9");

        verify(recipeReactiveRepository)
                .findById("9");


    }

    @Test
    void doDeleteByRecipeIDAndIngredientID() {

        Recipe recipe = Recipe.builder().id("33").build();

        Ingredient i1 = new Ingredient();
        i1.setId("1");

        Ingredient i2 = new Ingredient();
        i1.setId("1");

        Ingredient i3 = new Ingredient();
        i1.setId("3");
        Set<Ingredient> ingredientSet = new HashSet<>();

        ingredientSet.add(i1);
        ingredientSet.add(i2);
        ingredientSet.add(i3);

        ingredientSet.forEach(i -> recipe.addIngredient(i));

        Optional<Recipe> recipeOptional = Optional.of(recipe);

        when(recipeReactiveRepository.findById(anyString())).thenReturn(Mono.just(recipe));
        when(recipeReactiveRepository.save(any())).thenReturn(Mono.just(recipe));
        IngredientCommand command = IngredientCommand.builder().id("2").recipeID("33").build();

        ingredientService.removeIngredientCommand(command);

        verify(recipeReactiveRepository, times(1)).findById(anyString());
        verify(recipeReactiveRepository, times(1)).save(any(Recipe.class));

    }

//    @Test
//    public void testSaveNewIngredient(){
//
//
//        Recipe recipe = Recipe.builder().id("33").build();
//
//        Ingredient i1 = new Ingredient();
//        i1.setId("1");
//
//        Ingredient i2 = new Ingredient();
//        i1.setId("1");
//
//        Ingredient i3 = new Ingredient();
//        i1.setId("3");
//        i3.setQuantity(3);
//        Set<Ingredient> ingredientSet = new HashSet<>();
//
//        ingredientSet.add(i1);
//        ingredientSet.add(i2);
//        ingredientSet.add(i3);
//
//
//        ingredientSet.forEach(i -> recipe.addIngredient(i));
//
//        IngredientCommand newIngredient = new IngredientCommand();
//        newIngredient.setId("4");
//        newIngredient.setRecipeID("33");
//
//        when(recipeReactiveRepository.findById(anyString())).thenReturn(Mono.just(recipe));
//        when(recipeReactiveRepository.save(any())).thenReturn(Mono.just(recipe));
//        Mono<IngredientCommand> monoCommand = ingredientService.saveIngredientCommand(newIngredient);
//
//        monoRecipe.subscribe(ingredients)
//
//
//
//    }

}
