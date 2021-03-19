package com.ibm.petergreaves.recipe.converters;

import com.ibm.petergreaves.recipe.commands.NotesCommand;
import com.ibm.petergreaves.recipe.domain.Notes;

import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class NotesToNotesCommand implements Converter<Notes, NotesCommand> {


    @Override

    @Synchronized
    public NotesCommand convert(Notes notes) {
        if (notes == null){

            return null;
        }

        return NotesCommand
                .builder()
                .id(notes.getId())
                .recipeNotes(notes.getRecipeNotes())
                .build();

    }
}
