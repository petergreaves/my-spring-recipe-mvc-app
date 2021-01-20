package com.ibm.petergreaves.recipe.converters;

import com.ibm.petergreaves.recipe.commands.UnitOfMeasureCommand;
import com.ibm.petergreaves.recipe.domain.Category;
import com.ibm.petergreaves.recipe.domain.UnitOfMeasure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnitOfMeasureCommandToUnitOfMeasureTest {

    UnitOfMeasureCommandToUnitOfMeasure uomConverter;

    UnitOfMeasure expected=null;
    final Long idExpected=44L;
    final String desc = "description";

    @BeforeEach
    void setup(){
        expected= new UnitOfMeasure();
        expected.setId(idExpected);
        expected.setDescription(desc);

        uomConverter = new UnitOfMeasureCommandToUnitOfMeasure();

    }

    @Test
    void returnsNullForNull(){

        assertNull(uomConverter.convert(null));

    }

    @Test
    void returnsEmptyForEmpty(){

        assertNotNull(uomConverter.convert(UnitOfMeasureCommand.builder().build()));

    }

    @Test
    void convert() {
        UnitOfMeasureCommand uomCommand =new UnitOfMeasureCommand();
        uomCommand = UnitOfMeasureCommand.builder().id(idExpected).description(desc).build();

        UnitOfMeasure fromConverter = uomConverter.convert(uomCommand);

        assertEquals(fromConverter.getDescription(),expected.getDescription());
        assertEquals(fromConverter.getId(),expected.getId());
    }
}
