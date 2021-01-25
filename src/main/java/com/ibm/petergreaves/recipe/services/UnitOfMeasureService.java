package com.ibm.petergreaves.recipe.services;

import com.ibm.petergreaves.recipe.commands.UnitOfMeasureCommand;

import java.util.Set;

public interface UnitOfMeasureService {

    Set<UnitOfMeasureCommand> listUnitOfMeasures();
}
