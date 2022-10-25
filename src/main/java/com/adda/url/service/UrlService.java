package com.adda.url.service;

import com.adda.url.Url;
import com.adda.url.dto.UrlDto;
import org.springframework.stereotype.Service;

@Service
public interface UrlService {
    Url generateShortLink(UrlDto urlDto);

    Url persistShortLink(Url url);

    Url getEncodedUrl(String url);

    void deleteShortLink(Url url);
}