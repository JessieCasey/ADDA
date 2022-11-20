package com.adda.auth.service;

import com.adda.auth.dto.SignInDTO;
import com.adda.auth.dto.SignupDTO;
import com.adda.auth.jwt.JwtResponse;

import javax.validation.Valid;

public interface AuthService {
    JwtResponse authenticate(@Valid SignInDTO loginRequest);

    void register(SignupDTO request, String url);
}
