package com.adda.user.history;

import com.adda.advert.Advert;
import com.adda.url.service.UrlServiceImpl;

import java.util.List;

/**
 * The HistoryService interface is required to create HistoryServiceImpl {@link HistoryServiceImpl}
 */

public interface HistoryService {
    List<Advert> getUserHistory(long id);
}
