package com.adda.auth.service;

import com.adda.auth.dto.SignInDTO;
import com.adda.auth.dto.SignupDTO;
import com.adda.auth.jwt.JwtResponse;

import javax.validation.Valid;

/**
 * The AuthService interface is required to create AuthServiceImpl {@link AuthServiceImpl}
 */

public interface AuthService {
    JwtResponse authenticate(@Valid SignInDTO loginRequest);

    void register(SignupDTO request, String url);
}
