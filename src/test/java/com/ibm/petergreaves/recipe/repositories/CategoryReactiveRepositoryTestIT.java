package com.ibm.petergreaves.recipe.repositories;

import com.ibm.petergreaves.recipe.domain.Category;
import com.ibm.petergreaves.recipe.repositories.reactive.CategoryReactiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataMongoTest
public class CategoryReactiveRepositoryTestIT {


    @Autowired
    CategoryReactiveRepository categoryReactiveRepository;

    @BeforeEach
    public void clear(){

        categoryReactiveRepository.deleteAll().block();
    }

    @Test
    public void saveNewCategory(){

        long k = categoryReactiveRepository.count().block();

        Category newCat = Category.builder().description("newCat").build();
        Category saved=categoryReactiveRepository.save(newCat).block();

        long newK= categoryReactiveRepository.count().block();

        assertAll(
                () -> assertEquals(k, newK-1),
                () -> assertNotNull(saved)
        );
    }

    @Test
    public void findByDescription(){


        Category newCat = Category.builder().description("newCat123").build();
        Category saved=categoryReactiveRepository.save(newCat).block();
        Category retrieved = categoryReactiveRepository.findByDescription("newCat123").block();

        assertAll(
                () -> assertEquals(retrieved.getId(), saved.getId()),
                () -> assertNotNull(retrieved)
        );
    }

}
