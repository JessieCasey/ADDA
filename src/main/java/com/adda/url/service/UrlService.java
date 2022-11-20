package com.adda.url.service;

import com.adda.url.Url;
import com.adda.url.dto.UrlDto;
import org.springframework.stereotype.Service;

/**
 * The UrlService interface is required to create UrlServiceImpl {@link UrlServiceImpl}
 */

@Service
public interface UrlService {
    Url generateShortLink(UrlDto urlDto);

    Url persistShortLink(Url url);

    Url getEncodedUrl(String url);

    void deleteShortLink(Url url);
}