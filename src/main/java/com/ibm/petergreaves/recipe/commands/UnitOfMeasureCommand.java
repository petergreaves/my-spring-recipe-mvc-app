package com.ibm.petergreaves.recipe.commands;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor@AllArgsConstructor
public class UnitOfMeasureCommand {

    private Long id;
    private String description;
}
