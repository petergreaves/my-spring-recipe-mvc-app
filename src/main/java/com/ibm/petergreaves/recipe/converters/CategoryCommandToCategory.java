package com.ibm.petergreaves.recipe.converters;

import com.ibm.petergreaves.recipe.commands.CategoryCommand;
import com.ibm.petergreaves.recipe.domain.Category;
import com.sun.istack.Nullable;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CategoryCommandToCategory implements Converter<CategoryCommand, Category> {

    @Override
    @Nullable
    @Synchronized
    public Category convert(CategoryCommand categoryCommand) {
        if (null==categoryCommand){
            return null;
        }
        final Category category = new Category();
        category.setId(categoryCommand.getId());
        category.setDescription(categoryCommand.getDescription());

        return  category;
    }
}
