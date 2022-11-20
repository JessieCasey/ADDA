package com.adda.auth.token.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * The TokenRefreshResponse class is required to represent server response after refreshing token.
 */


@Getter
@Setter
public class TokenRefreshResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";

    public TokenRefreshResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}