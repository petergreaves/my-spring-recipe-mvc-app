package com.ibm.petergreaves.recipe.commands;

import com.ibm.petergreaves.recipe.domain.Recipe;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotesCommand {

    private String recipeNotes;
    private Long id;
}
