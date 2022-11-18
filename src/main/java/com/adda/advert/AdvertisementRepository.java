package com.adda.advert;

import com.adda.advert.category.CategoriesEntity;
import com.adda.user.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface AdvertisementRepository extends CrudRepository<AdvertisementEntity, UUID> {

    AdvertisementEntity getById(UUID id);

    void deleteById(UUID id);

    List<AdvertisementEntity> findAllByPriceBetweenAndCategory(Integer startPrice, Integer endPrice, CategoriesEntity category);

    List<AdvertisementEntity> findAllByPriceBetween(Integer startPrice, Integer endPrice);

    boolean existsByTitleAndUsername(String advertisementName, String username);

    List<AdvertisementEntity> findAllByCategory(CategoriesEntity category);

    List<AdvertisementEntity> findAllByUser(User user);

}
