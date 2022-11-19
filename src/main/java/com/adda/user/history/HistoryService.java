package com.adda.user.history;

import com.adda.advert.Advertisement;

import java.util.List;

public interface HistoryService {
    List<Advertisement> getUserHistory(long id);
}
