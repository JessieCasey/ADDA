package com.adda.user.history;

import com.adda.advert.Advert;

import java.util.List;

public interface HistoryService {
    List<Advert> getUserHistory(long id);
}
