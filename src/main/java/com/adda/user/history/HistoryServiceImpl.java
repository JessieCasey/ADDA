package com.adda.user.history;

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
