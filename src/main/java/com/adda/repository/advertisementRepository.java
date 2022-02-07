package com.adda.repository;

import com.adda.domain.AdvertisementEntity;
import org.springframework.data.repository.CrudRepository;

public interface advertisementRepository extends CrudRepository<AdvertisementEntity, Long> {
    AdvertisementEntity findByUsername(String username);
}
