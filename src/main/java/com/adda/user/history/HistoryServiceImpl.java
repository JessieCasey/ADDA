package com.adda.user.history;

import com.adda.advert.Advert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HistoryServiceImpl implements HistoryService {

    private final HistoryRepository historyRepository;

    @Autowired
    public HistoryServiceImpl(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    @Override
    public List<Advert> getUserHistory(long userId) {
        if (!historyRepository.existsById(userId)) {
            return new ArrayList<>();
        } else {
            return historyRepository.findById(userId).orElseThrow(IllegalArgumentException::new).getAdverts();
        }
    }
}
