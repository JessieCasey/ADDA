package com.adda.repository;

import com.adda.domain.AdvertisementEntity;
import com.adda.domain.HistoryEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface HistoryRepository extends CrudRepository<HistoryEntity, Long> {
    boolean existsByIdAndAdvertsIsContaining(long userId, AdvertisementEntity id);

    Optional<HistoryEntity> findById(long userId);
}
