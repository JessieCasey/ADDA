package com.adda.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDTO {
    private String usernameOrEmail;
    private String password;
}