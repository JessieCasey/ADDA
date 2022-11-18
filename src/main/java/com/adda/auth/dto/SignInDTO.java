package com.adda.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignInDTO {
    private String email;
    private String password;
}