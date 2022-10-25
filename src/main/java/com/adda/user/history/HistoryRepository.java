package com.adda.user.history;

import com.adda.advert.AdvertisementEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface HistoryRepository extends CrudRepository<HistoryEntity, Long> {
    boolean existsByIdAndAdvertsIsContaining(long userId, AdvertisementEntity id);

    Optional<HistoryEntity> findById(long userId);
}
