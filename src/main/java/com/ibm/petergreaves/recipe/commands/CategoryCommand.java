package com.ibm.petergreaves.recipe.commands;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryCommand {

    private String description;
    private String id;
}
