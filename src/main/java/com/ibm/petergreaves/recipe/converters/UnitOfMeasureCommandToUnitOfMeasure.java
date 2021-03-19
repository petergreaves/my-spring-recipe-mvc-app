package com.ibm.petergreaves.recipe.converters;

import com.ibm.petergreaves.recipe.commands.CategoryCommand;
import com.ibm.petergreaves.recipe.commands.UnitOfMeasureCommand;
import com.ibm.petergreaves.recipe.domain.Category;
import com.ibm.petergreaves.recipe.domain.UnitOfMeasure;

import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UnitOfMeasureCommandToUnitOfMeasure implements Converter<UnitOfMeasureCommand, UnitOfMeasure> {

    @Override

    @Synchronized
    public UnitOfMeasure convert(UnitOfMeasureCommand unitOfMeasureCommand) {
        if (null == unitOfMeasureCommand) {
            return null;
        }
        UnitOfMeasure oum =new UnitOfMeasure();
        oum.setDescription(unitOfMeasureCommand.getDescription());
        oum.setId(unitOfMeasureCommand.getId());
        return oum;

    }
}
