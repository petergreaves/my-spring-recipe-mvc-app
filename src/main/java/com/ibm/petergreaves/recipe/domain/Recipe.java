package com.ibm.petergreaves.recipe.domain;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"notes"})
@Entity
@Builder
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private Byte[] image;
    private Integer cookTime;
    private Integer prepTime;
    private Integer servings;
    private String source;
    private String url;
    private String directions;
    private String description;
    private String title;


    @ManyToMany
    @JoinTable(name="recipe_category",
        joinColumns = @JoinColumn(name="recipe_id"),
            inverseJoinColumns = @JoinColumn(name="category_id")
    )
    private Set<Category> categories= new HashSet<>();

    @Enumerated(value=EnumType.STRING)
    private Difficulty difficulty;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "recipe")
    private Set<Ingredient> ingredients = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL)
    private Notes notes;

    public void setNotes(Notes notes) {
        this.notes = notes;
        notes.setRecipe(this);
    }



    public Recipe addIngredient(Ingredient i){
        i.setRecipe(this);
        this.ingredients.add(i);
        return this;
    }


}
