package com.adda.url.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * The UrlDto class is required if we want to create the short link.
 */

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UrlDto {
    private String url;
    private String expirationDate;  //optional

    public UrlDto(String url, String expirationDate) {
        this.url = url;
        this.expirationDate = expirationDate;
    }
}