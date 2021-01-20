package com.ibm.petergreaves.recipe.converters;

import com.ibm.petergreaves.recipe.commands.NotesCommand;
import com.ibm.petergreaves.recipe.commands.RecipeCommand;
import com.ibm.petergreaves.recipe.commands.UnitOfMeasureCommand;
import com.ibm.petergreaves.recipe.domain.Notes;
import com.ibm.petergreaves.recipe.domain.Recipe;
import com.ibm.petergreaves.recipe.domain.UnitOfMeasure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotesCommandToNotesTest {
    NotesCommandToNotes notesConverter;

    Notes expected=null;
    final Long idExpected=44L;

    final String notes = "some recipe notes";


    @BeforeEach
    void setup(){
        expected= new Notes();
        expected.setId(idExpected);
        expected.setRecipeNotes(notes);


        notesConverter = new NotesCommandToNotes();

    }

    @Test
    void returnsNullForNull(){

        assertNull(notesConverter.convert(null));

    }

    @Test
    void returnsEmptyForEmpty(){

        assertNotNull(notesConverter.convert(NotesCommand.builder().build()));

    }


    @Test
    void convert() {


        NotesCommand notesCommand =NotesCommand.builder().id(idExpected).recipeNotes(notes).build();
        Notes fromConverter = notesConverter.convert(notesCommand);

        assertEquals(fromConverter.getRecipeNotes(),expected.getRecipeNotes());
        assertEquals(fromConverter.getId(),expected.getId());
        assertNull(fromConverter.getRecipe());
    }
}
