package com.ibm.petergreaves.recipe.bootstrap;

import com.ibm.petergreaves.recipe.domain.Category;
import com.ibm.petergreaves.recipe.domain.UnitOfMeasure;
import com.ibm.petergreaves.recipe.repositories.CategoryRepository;
import com.ibm.petergreaves.recipe.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
@Profile({"dev","prod"})
public class RecipeBootstrapMySQL implements ApplicationListener<ContextRefreshedEvent> {

    private final UnitOfMeasureRepository uomRepository;
    private final CategoryRepository categoryRepository;

    public RecipeBootstrapMySQL(UnitOfMeasureRepository uomRepository, CategoryRepository categoryRepository) {
        this.uomRepository = uomRepository;
        this.categoryRepository = categoryRepository;
    }


    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (categoryRepository.count() == 0){
            categoryRepository.saveAll(getCategories());
        }

        if (uomRepository.count() == 0){
            uomRepository.saveAll(getUoMs());
        }

    }


    private Set<Category> getCategories(){
        Set<Category> cats = new HashSet<>();
        Category american = Category.builder().description("American").build();
        Category french = Category.builder().description("French").build();
        Category mexican = Category.builder().description("Mexican").build();

        cats.add(french);
        cats.add(american);
        cats.add(mexican);

        return cats;

    }

    private Set<UnitOfMeasure> getUoMs(){

        Set<UnitOfMeasure> uoms = new HashSet<>();

        UnitOfMeasure each = UnitOfMeasure.builder().description("each").build();
        UnitOfMeasure cup = UnitOfMeasure.builder().description("cup").build();
        UnitOfMeasure teaspoon = UnitOfMeasure.builder().description("teaspoon").build();
        UnitOfMeasure gram = UnitOfMeasure.builder().description("gram").build();
        UnitOfMeasure tablespoon = UnitOfMeasure.builder().description("tablespoon").build();

        uoms.add(each);
        uoms.add(cup);
        uoms.add(teaspoon);
        uoms.add(gram);
        uoms.add(tablespoon);

        return uoms;

    }




}
