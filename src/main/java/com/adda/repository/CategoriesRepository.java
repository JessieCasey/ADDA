package com.adda.repository;

import com.adda.domain.CategoriesEntity;
import org.springframework.data.repository.CrudRepository;

public interface CategoriesRepository extends CrudRepository<CategoriesEntity, Long> {
    CategoriesEntity findByCategoryName(String category_name);
}
