package com.adda.advert.category;

import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Long> {
    Category findByCategoryName(String category_name);

    boolean existsByCategoryName(String name);
}
