package com.adda.auth;

import com.adda.auth.dto.SignInDTO;
import com.adda.auth.dto.SignupDTO;
import com.adda.auth.jwt.JwtResponse;
import com.adda.auth.jwt.JwtUtils;
import com.adda.auth.jwt.MessageResponse;
import com.adda.auth.service.AuthService;
import com.adda.auth.token.RefreshToken;
import com.adda.auth.token.dto.TokenRefreshRequest;
import com.adda.auth.token.dto.TokenRefreshResponse;
import com.adda.auth.token.exception.TokenRefreshException;
import com.adda.auth.token.service.RefreshTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

    private final RefreshTokenService refreshTokenService;
    private final AuthService authService;
    private final JwtUtils jwtUtils;

    public AuthController(RefreshTokenService refreshTokenService, AuthService authService, JwtUtils jwtUtils) {
        this.refreshTokenService = refreshTokenService;
        this.authService = authService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody SignInDTO loginRequest) {
        JwtResponse authenticate = authService.authenticate(loginRequest);
        return ResponseEntity.ok(authenticate);
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken).map(refreshTokenService::verifyExpiration).map(RefreshToken::getUser).map(user -> {
            String token = jwtUtils.generateTokenFromUsername(user.getUsername());
            return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
        }).orElseThrow(() -> new TokenRefreshException(requestRefreshToken, "Refresh token is not in database!"));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupDTO signUpRequest, HttpServletRequest request) {
        authService.register(signUpRequest, getSiteURL(request));
        return ResponseEntity.ok(new MessageResponse("User registered successfully! Please, verify your email address!"));
    }

    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }
}