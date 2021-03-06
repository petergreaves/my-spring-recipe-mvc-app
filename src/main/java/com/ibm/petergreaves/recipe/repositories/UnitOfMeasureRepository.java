package com.ibm.petergreaves.recipe.repositories;

import com.ibm.petergreaves.recipe.domain.Category;
import com.ibm.petergreaves.recipe.domain.UnitOfMeasure;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UnitOfMeasureRepository extends CrudRepository<UnitOfMeasure, Long> {

    Optional<UnitOfMeasure> findByDescription(String description);

}
