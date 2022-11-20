package com.adda.auth.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * The SignInDTO class is required to authenticate into the system.
 */

@Getter
@Setter
public class SignInDTO {
    private String email;
    private String password;
}