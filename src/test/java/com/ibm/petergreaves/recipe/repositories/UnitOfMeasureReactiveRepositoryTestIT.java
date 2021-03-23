package com.ibm.petergreaves.recipe.repositories;

import com.ibm.petergreaves.recipe.domain.Category;
import com.ibm.petergreaves.recipe.domain.Recipe;
import com.ibm.petergreaves.recipe.domain.UnitOfMeasure;
import com.ibm.petergreaves.recipe.repositories.reactive.RecipeReactiveRepository;
import com.ibm.petergreaves.recipe.repositories.reactive.UnitOfMeasureReactiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataMongoTest
public class UnitOfMeasureReactiveRepositoryTestIT {

    public static final String EACH = "Each";
    @Autowired
    UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;


    @BeforeEach
    public void clear(){

        unitOfMeasureReactiveRepository.deleteAll().block();
    }

    @Test
    public void findByDescription(){


        UnitOfMeasure
                newUOM = UnitOfMeasure.builder().description(EACH).build();
        UnitOfMeasure saved=unitOfMeasureReactiveRepository.save(newUOM).block();
        UnitOfMeasure retrieved = unitOfMeasureReactiveRepository.findByDescription(EACH).block();

        assertAll(
                () -> assertEquals(retrieved.getId(), saved.getId()),
                () -> assertEquals(retrieved.getDescription(), EACH)
        );
    }

    @Test
    public void createNewUOM(){

        long k = unitOfMeasureReactiveRepository.count().block();

        UnitOfMeasure newUOM = UnitOfMeasure.builder().description("new uom").build();

        UnitOfMeasure savedNewUOM = unitOfMeasureReactiveRepository.save(newUOM).block();
        long newK = unitOfMeasureReactiveRepository.count().block();

        assertAll(
                () -> assertEquals(1, newK),
                () -> assertEquals(k, newK-1),
                () -> assertNotNull(savedNewUOM)
        );
    }
}
