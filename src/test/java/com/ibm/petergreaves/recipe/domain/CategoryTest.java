package com.ibm.petergreaves.recipe.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
    }

    @Test
    void getId() {
        Long idVal = 5L;
        category.setId(idVal);
        assertEquals(idVal, category.getId());

    }

    @Test
    void getDescription() {
    }
}
