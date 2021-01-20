package com.ibm.petergreaves.recipe.converters;

import com.ibm.petergreaves.recipe.commands.CategoryCommand;
import com.ibm.petergreaves.recipe.domain.Category;
import com.sun.istack.Nullable;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CategoryToCategoryCommand implements Converter<Category, CategoryCommand> {

    @Override
    @Nullable
    @Synchronized
    public CategoryCommand convert(Category category) {
        if (category == null){

            return null;
        }

        return CategoryCommand
                .builder()
                .id(category.getId())
                .description(category.getDescription())
                .build();
    }
}
