package com.ibm.petergreaves.recipe.utils;

import com.ibm.petergreaves.recipe.domain.Ingredient;

import java.util.Comparator;

public class IngredientsComparator implements Comparator<Ingredient> {

    @Override
    public int compare(Ingredient o1, Ingredient o2) {
        return o1.getDescription().compareTo(o2.getDescription());
    }
}
