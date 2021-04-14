package com.ibm.petergreaves.recipe.controllers;

import com.ibm.petergreaves.recipe.config.WebConfig;
import com.ibm.petergreaves.recipe.domain.Recipe;
import com.ibm.petergreaves.recipe.services.RecipeService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import reactor.core.publisher.Flux;

import static org.mockito.Mockito.when;

public class RouterFunctionTest {

    WebTestClient webTestClient;

    @Mock
    RecipeService recipeService;

    private AutoCloseable closeable;

    private RouterFunction<?> recipeRouterFunction ;

    @BeforeEach
    public void setup() throws Exception{
        closeable = MockitoAnnotations.openMocks(this);

        WebConfig webConfig = new WebConfig();
        recipeRouterFunction=webConfig.routes(recipeService);

        webTestClient= WebTestClient.bindToRouterFunction(recipeRouterFunction).build();

    }

    @Test
    public void getRecipesIsOK() throws Exception {

        when(recipeService.getRecipes()).thenReturn(Flux.just());

        webTestClient.get()
                .uri("/api/recipes")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody();
    }

    @Test
    public void getRecipesWithData() throws Exception {

        when(recipeService.getRecipes()).thenReturn(Flux.just(Recipe.builder().title("recipe1").build(), new Recipe()));

        webTestClient.get()
                .uri("/api/recipes")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Recipe.class);
    }
}
