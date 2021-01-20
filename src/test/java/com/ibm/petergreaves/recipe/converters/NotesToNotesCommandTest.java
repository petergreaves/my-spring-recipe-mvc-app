package com.ibm.petergreaves.recipe.converters;

import com.ibm.petergreaves.recipe.commands.NotesCommand;
import com.ibm.petergreaves.recipe.commands.RecipeCommand;
import com.ibm.petergreaves.recipe.domain.Notes;
import com.ibm.petergreaves.recipe.domain.Recipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotesToNotesCommandTest {

    NotesToNotesCommand notesConverter;

    NotesCommand expected=null;
    final Long idExpected=44L;
    final Long recipeID=45499L;
    final String recipeNotes = "some recipe notes";


    @BeforeEach
    void setup(){

        expected= new NotesCommand();
        expected.setId(idExpected);
        expected.setRecipeNotes(recipeNotes);


        notesConverter = new NotesToNotesCommand();
    }

    @Test
    void returnsNullForNull(){

        assertNull(notesConverter.convert(null));

    }

    @Test
    void returnsEmptyForEmpty(){

        assertNotNull(notesConverter.convert(new Notes()));

    }

    @Test
    void convert(){

        Notes notes =new Notes();
        notes.setId(idExpected);
        notes.setRecipeNotes(recipeNotes);
        NotesCommand fromConverter = notesConverter.convert(notes);

        assertEquals(fromConverter.getRecipeNotes(),expected.getRecipeNotes());
        assertEquals(fromConverter.getId(),expected.getId());


    }

}
