package com.adda.url.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UrlResponseDto {
    private String originalUrl;
    private String shortLink;
    private LocalDateTime expirationDate;
}