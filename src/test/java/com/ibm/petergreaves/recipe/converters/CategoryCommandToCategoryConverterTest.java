package com.ibm.petergreaves.recipe.converters;

import com.ibm.petergreaves.recipe.commands.CategoryCommand;
import com.ibm.petergreaves.recipe.domain.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class CategoryCommandToCategoryConverterTest {


    CategoryCommandToCategory categoryCommandConverter;

    Category expected=null;
    final Long idExpected=44L;
    final String desc = "description";

    @BeforeEach
    void setup(){
        expected= new Category();
        expected.setId(idExpected);
        expected.setDescription(desc);

        categoryCommandConverter = new CategoryCommandToCategory();

    }

    @Test
    void testReturnsNullForNull(){

        assertNull(categoryCommandConverter.convert(null));
    }

    @Test
    void testReturnsEmptyForEmpty(){

        assertNotNull(categoryCommandConverter.convert(new CategoryCommand()));
    }


    @Test
    void testConvert() {

        CategoryCommand categoryCommand = new CategoryCommand();
        categoryCommand.setDescription(desc);
        categoryCommand.setId(idExpected);

        Category converted = categoryCommandConverter.convert(categoryCommand);

        assertEquals(expected.getDescription(), converted.getDescription());
        assertEquals(expected.getId(), converted.getId());
    }
}
