package com.ibm.petergreaves.recipe.converters;

import com.ibm.petergreaves.recipe.commands.UnitOfMeasureCommand;
import com.ibm.petergreaves.recipe.domain.UnitOfMeasure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnitOfMeasureToUnitOfMeasureCommandTest {

    UnitOfMeasureToUnitOfMeasureCommand uomConverter;
    UnitOfMeasureCommand expected;

    final String idExpected="44";
    final String desc = "description";

    @BeforeEach
    void setup(){
        expected = UnitOfMeasureCommand.builder().id(idExpected).description(desc).build();
        uomConverter=new UnitOfMeasureToUnitOfMeasureCommand();

    }

    @Test
    void returnsNullForNull(){

        assertNull(uomConverter.convert(null));

    }

    @Test
    void returnsEmptyForEmpty(){

        assertNotNull(uomConverter.convert(new UnitOfMeasure()));

    }


    @Test
    void convert() {
        UnitOfMeasure uom =new UnitOfMeasure();
        uom.setId(idExpected);
        uom.setDescription(desc);

        UnitOfMeasureCommand fromConverter = uomConverter.convert(uom);

        assertEquals(fromConverter.getDescription(),expected.getDescription());
        assertEquals(fromConverter.getId(),expected.getId());



    }
}
