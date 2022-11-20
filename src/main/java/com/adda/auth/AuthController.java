package com.adda.auth;

import com.adda.advice.MessageResponse;
import com.adda.auth.dto.SignInDTO;
import com.adda.auth.dto.SignupDTO;
import com.adda.auth.jwt.JwtResponse;
import com.adda.auth.jwt.JwtUtils;
import com.adda.auth.service.AuthService;
import com.adda.auth.token.RefreshToken;
import com.adda.auth.token.dto.TokenRefreshRequest;
import com.adda.auth.token.dto.TokenRefreshResponse;
import com.adda.auth.token.exception.TokenRefreshException;
import com.adda.auth.token.service.RefreshTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

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

    /**
     * Constructor for {@link AuthController}.
     *
     * @param refreshTokenService {@link RefreshTokenService}
     * @param authService         {@link AuthService}
     * @param jwtUtils            {@link JwtUtils}
     */
    @Autowired
    public AuthController(RefreshTokenService refreshTokenService, AuthService authService, JwtUtils jwtUtils) {
        this.refreshTokenService = refreshTokenService;
        this.authService = authService;
        this.jwtUtils = jwtUtils;
    }

    /**
     * Method that authenticate user into the system. {@link com.adda.user.User}
     *
     * @param loginRequest DTO to authenticate data. {@link SignInDTO}
     * @return ResponseEntity<JwtResponse> object in case of success. {@link ResponseEntity<JwtResponse>}
     * @author Artem Komarov
     */
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody SignInDTO loginRequest, WebRequest request) {
        try {
            JwtResponse authenticate = authService.authenticate(loginRequest);
            return ResponseEntity.ok(authenticate);
        } catch (Exception e) {
            log.error("Error in method 'addAdvert': " + e.getMessage());
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage(), request));
        }

    }

    /**
     * Method that refreshing the current token to avoid re-authentication user into the system.
     *
     * @param request refresh token. {@link TokenRefreshRequest}
     * @return ResponseEntity<JwtResponse> object in case of success. {@link ResponseEntity}
     * @author Artem Komarov
     */
    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request, WebRequest webRequest) {
        try {
            String requestRefreshToken = request.getRefreshToken();

            return refreshTokenService.findByToken(requestRefreshToken).map(refreshTokenService::verifyExpiration).map(RefreshToken::getUser).map(user -> {
                String token = jwtUtils.generateTokenFromUsername(user.getUsername());
                return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
            }).orElseThrow(() -> new TokenRefreshException(requestRefreshToken, "Refresh token is not in database!"));

        } catch (Exception e) {
            log.error("Error in method 'addAdvert': " + e.getMessage());
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage(), webRequest));
        }
    }

    /**
     * Method register user in the system. {@link com.adda.user.User}
     *
     * @param signUpRequest DTO to register user. {@link SignupDTO}
     * @return ResponseEntity<MessageResponse> object in case of success. {@link ResponseEntity<MessageResponse>}
     * @author Artem Komarov
     */
    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignupDTO signUpRequest, HttpServletRequest request, WebRequest webRequest) {
        try {
            authService.register(signUpRequest, getSiteURL(request));
            return ResponseEntity.ok(
                    new MessageResponse("User registered successfully! Please, verify your email address!", webRequest));

        } catch (Exception e) {
            log.error("Error in method 'addAdvert': " + e.getMessage());
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage(), webRequest));
        }
    }

    /**
     * Method that getting the URL path.
     *
     * @param request DTO to register user. {@link HttpServletRequest}
     * @return URL in case of success
     */
    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }
}