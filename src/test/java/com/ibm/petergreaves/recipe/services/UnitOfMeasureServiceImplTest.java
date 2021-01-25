package com.ibm.petergreaves.recipe.services;

import com.ibm.petergreaves.recipe.commands.UnitOfMeasureCommand;
import com.ibm.petergreaves.recipe.converters.IngredientToIngredientCommand;
import com.ibm.petergreaves.recipe.converters.UnitOfMeasureToUnitOfMeasureCommand;
import com.ibm.petergreaves.recipe.domain.UnitOfMeasure;
import com.ibm.petergreaves.recipe.repositories.RecipeRepository;
import com.ibm.petergreaves.recipe.repositories.UnitOfMeasureRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

public class UnitOfMeasureServiceImplTest {

    UnitOfMeasureServiceImpl unitOfMeasureService;

    @Mock
    UnitOfMeasureRepository uomRepository;

    private AutoCloseable closeable;

    private  Set<UnitOfMeasure> uoms;

    @BeforeEach
    public void openMocks() {
        closeable = MockitoAnnotations.openMocks(this);
        unitOfMeasureService = new UnitOfMeasureServiceImpl(uomRepository, new UnitOfMeasureToUnitOfMeasureCommand());
        uoms = new HashSet<>();
    }

    @AfterEach
    public void releaseMocks() throws Exception {
        closeable.close();
    }

    @Test
    void findAllUnitOfMeasures(){

     UnitOfMeasure cup = UnitOfMeasure.builder().id(1L).description("cup").build();
     UnitOfMeasure each = UnitOfMeasure.builder().id(2L).description("each").build();
     UnitOfMeasure spoon = UnitOfMeasure.builder().id(3L).description("spoon").build();

     uoms.add(cup);
     uoms.add(each);
     uoms.add(spoon);

    when(uomRepository.findAll()).thenReturn(uoms);
    Set<UnitOfMeasureCommand> commands = unitOfMeasureService.listUnitOfMeasures();
    assertEquals(uoms.size(), 3);
    }

}
