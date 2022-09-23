package com.adda.repository;

import com.adda.domain.HistoryEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface HistoryRepository extends CrudRepository<HistoryEntity, Long> {
    boolean existsByIdAndUser(UUID id, long id2);
}
