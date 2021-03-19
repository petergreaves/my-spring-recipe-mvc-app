package com.ibm.petergreaves.recipe.commands;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor@AllArgsConstructor
public class UnitOfMeasureCommand {

    private String id;
    private String description;
}
