package com.ibm.petergreaves.recipe.converters;

import com.ibm.petergreaves.recipe.commands.UnitOfMeasureCommand;
import com.ibm.petergreaves.recipe.domain.UnitOfMeasure;
import com.sun.istack.Nullable;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UnitOfMeasureToUnitOfMeasureCommand implements Converter<UnitOfMeasure, UnitOfMeasureCommand> {

    @Override
    @Nullable
    @Synchronized
    public UnitOfMeasureCommand convert(UnitOfMeasure unitOfMeasure) {
        if (null == unitOfMeasure) {
            return null;
        }
        UnitOfMeasureCommand oumCommand =new UnitOfMeasureCommand();
        oumCommand.setDescription(unitOfMeasure.getDescription());
        oumCommand.setId(unitOfMeasure.getId());
        return oumCommand;

    }
}
