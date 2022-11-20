package com.adda.url.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * The UrlDto class is required to represent Error related to the URL short link.
 */

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UrlErrorResponseDto {
    private String status;
    private String error;
}