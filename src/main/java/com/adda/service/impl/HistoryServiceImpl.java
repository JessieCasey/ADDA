package com.adda.service.impl;

import com.adda.domain.HistoryEntity;
import com.adda.repository.HistoryRepository;
import com.adda.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HistoryServiceImpl implements HistoryService {

    private final HistoryRepository historyRepository;

    @Autowired
    public HistoryServiceImpl(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    @Override
    public HistoryEntity getUserHistory(long userId) {
        return historyRepository.findById(userId).orElseThrow(IllegalArgumentException::new);
    }
}
