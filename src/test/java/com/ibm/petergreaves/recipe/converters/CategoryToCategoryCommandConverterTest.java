package com.ibm.petergreaves.recipe.converters;

import com.ibm.petergreaves.recipe.commands.CategoryCommand;
import com.ibm.petergreaves.recipe.domain.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class CategoryToCategoryCommandConverterTest {

    @Autowired
    CategoryToCategoryCommand categoryCommandConverter;

    CategoryCommand expected=null;
    final String idExpected="44";
    final String desc = "description";

    @BeforeEach
    void setup(){
        expected= new CategoryCommand();
        expected.setId(idExpected);
        expected.setDescription(desc);

        categoryCommandConverter = new CategoryToCategoryCommand();

    }

    @Test
    void testReturnsNullForNull(){

        assertNull(categoryCommandConverter.convert(null));
    }

    @Test
    void testReturnsEmptyForEmpty(){

        assertNotNull(categoryCommandConverter.convert(new Category()));
    }


    @Test
    void testConvert() {

        Category category= new Category();
        category.setDescription(desc);
        category.setId(idExpected);

        CategoryCommand converted = categoryCommandConverter.convert(category);

        assertEquals(expected.getDescription(), converted.getDescription());
        assertEquals(expected.getId(), converted.getId());
    }

}
