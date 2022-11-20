package com.adda.url.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * The UrlResponseDto class is required if we want to represent the URL entity in flexible format.
 */

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UrlResponseDto {
    private String originalUrl;
    private String shortLink;
    private LocalDateTime expirationDate;
}