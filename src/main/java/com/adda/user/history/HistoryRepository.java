package com.adda.user.history;

import com.adda.advert.Advert;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface HistoryRepository extends CrudRepository<History, Long> {
    boolean existsByIdAndAdvertsIsContaining(long userId, Advert id);

    Optional<History> findById(long userId);
}
