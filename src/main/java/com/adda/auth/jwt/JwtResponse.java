package com.adda.auth.jwt;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * The JwtResponse class is required to provide response from authentication method.
 * {@link com.adda.auth.AuthController}
 */

@Getter
@Setter
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String refreshToken;
    private Long id;
    private String username;
    private String email;
    private List<String> roles;

    public JwtResponse(String accessToken,
                       String refreshToken,
                       Long id,
                       String username,
                       String email, List<String> roles) {
        this.token = accessToken;
        this.refreshToken = refreshToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }

}