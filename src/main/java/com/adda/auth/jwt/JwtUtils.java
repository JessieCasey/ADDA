package com.adda.auth.jwt;

import com.adda.user.service.UserDetailsImpl;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * The JwtUtils class is required to process with JWT tokens.
 */

@Component
@Slf4j
public class JwtUtils {

    @Value("${adda.app.jwtSecret}")
    private String jwtSecret;

    @Value("${adda.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    /**
     * Method that generating JWT tokens
     *
     * @param userPrincipal Authenticated user. {@link UserDetailsImpl}
     * @return JWT token of the user.
     */
    public String generateJwtToken(UserDetailsImpl userPrincipal) {
        return generateTokenFromUsername(userPrincipal.getEmail());
    }

    /**
     * Method that generating JWT tokens from username
     *
     * @param username Authenticated user's username.
     * @return JWT token of the user.
     */
    public String generateTokenFromUsername(String username) {
        return Jwts.builder().setSubject(username).setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)).signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    /**
     * Method that getting email from JWT token
     *
     * @param token Authenticated user's username.
     * @return email from JWT token.
     */
    public String getEmailFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Method that validating JWT tokens
     *
     * @param authToken JWT token.
     * @return boolean
     */
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}
