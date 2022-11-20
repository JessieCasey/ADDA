package com.adda.advert.category;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * The CategoryRepository interface {@link Category}
 */

@Repository
public interface CategoryRepository extends CrudRepository<Category, Long> {}
