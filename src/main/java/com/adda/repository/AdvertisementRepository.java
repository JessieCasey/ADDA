package com.adda.repository;

import com.adda.domain.AdvertisementEntity;
import com.adda.domain.CategoriesEntity;
import com.adda.domain.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface AdvertisementRepository extends CrudRepository<AdvertisementEntity, Long> {

    AdvertisementEntity findById(UUID id);

    void deleteById(UUID id);

    Iterable<AdvertisementEntity> findAllByPriceBetweenAndCategory(Integer startPrice, Integer endPrice, CategoriesEntity category);

    Iterable<AdvertisementEntity> findAllByPriceBetween(Integer startPrice, Integer endPrice);

    boolean existsByTitleAndUsername(String advertisementName, String username);

    Iterable<AdvertisementEntity> findAllByCategory(CategoriesEntity category);

    Iterable<AdvertisementEntity> findAllByUser(UserEntity user);

}
