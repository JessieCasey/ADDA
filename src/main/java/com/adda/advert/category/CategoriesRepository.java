package com.adda.advert.category;

import org.springframework.data.repository.CrudRepository;

public interface CategoriesRepository extends CrudRepository<CategoriesEntity, Long> {
    CategoriesEntity findByCategoryName(String category_name);

    boolean existsByCategoryName(String name);
}
