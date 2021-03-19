package com.ibm.petergreaves.recipe.converters;

import com.ibm.petergreaves.recipe.commands.NotesCommand;
import com.ibm.petergreaves.recipe.domain.Notes;

import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class NotesCommandToNotes implements Converter<NotesCommand, Notes> {


    @Override
    @Synchronized
    public Notes convert(NotesCommand notesCommand) {
        if (notesCommand == null){

            return null;
        }

        Notes notes = new Notes();

        notes.setRecipeNotes(notesCommand.getRecipeNotes());
        notes.setId(notesCommand.getId());

        return notes;
    }
}
