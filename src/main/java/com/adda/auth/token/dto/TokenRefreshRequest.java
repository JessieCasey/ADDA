package com.adda.auth.token.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * The TokenRefreshRequest class is required if we want to make refresh request.
 */

@Getter
@Setter
public class TokenRefreshRequest {
    @NotBlank
    private String refreshToken;
}