package com.adda.repository;

import com.adda.domain.AdvertisementEntity;
import com.adda.domain.CategoriesEntity;
import org.springframework.data.repository.CrudRepository;

public interface advertisementRepository extends CrudRepository<AdvertisementEntity, Long> {
    AdvertisementEntity findByUsername(String username);

    Iterable<AdvertisementEntity> findAllByPriceBetweenAndCategory(Integer startPrice, Integer endPrice, CategoriesEntity category);
    Iterable<AdvertisementEntity> findAllByPriceBetween(Integer startPrice, Integer endPrice);

    Iterable<AdvertisementEntity> findAllByTitle(String advertisementName);

    Iterable<AdvertisementEntity> findAllByCategory(CategoriesEntity category);
}
