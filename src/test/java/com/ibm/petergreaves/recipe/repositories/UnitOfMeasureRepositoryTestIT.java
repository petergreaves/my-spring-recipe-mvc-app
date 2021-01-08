package com.ibm.petergreaves.recipe.repositories;

import com.ibm.petergreaves.recipe.domain.UnitOfMeasure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class UnitOfMeasureRepositoryTestIT {

    @Autowired
    UnitOfMeasureRepository unitOfMeasureRepository;

    @BeforeEach
    void setUp() {
    }


    @Test
    void findByDescriptionCup() {
        Optional<UnitOfMeasure> optionalCup = unitOfMeasureRepository.findByDescription("Cup");
        assertEquals("Cup", optionalCup.get().getDescription());

    }
    @Test
    void findByDescriptionTeaspoon() {
        Optional<UnitOfMeasure> optionalCup = unitOfMeasureRepository.findByDescription("Teaspoon");
        assertEquals("Teaspoon", optionalCup.get().getDescription());


    }
}
