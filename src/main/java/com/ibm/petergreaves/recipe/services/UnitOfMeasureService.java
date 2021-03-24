package com.ibm.petergreaves.recipe.services;

import com.ibm.petergreaves.recipe.commands.UnitOfMeasureCommand;
import com.ibm.petergreaves.recipe.domain.UnitOfMeasure;
import reactor.core.publisher.Flux;

import java.util.Set;

public interface UnitOfMeasureService {

   // Set<UnitOfMeasureCommand> listUnitOfMeasures();
    Flux<UnitOfMeasureCommand> listUnitOfMeasures();
}
