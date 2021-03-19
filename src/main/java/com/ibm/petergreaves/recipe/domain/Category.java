package com.ibm.petergreaves.recipe.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude={"recipes"})
@Builder
@Document
public class Category {

    @Id
    private String id;

    private String description;

    @DBRef
    private Set<Recipe> recipes;



}
