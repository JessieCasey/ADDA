package com.adda.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * The SignInDTO class is required to sign up (register) into the system.
 */

@Getter
@Setter
public class SignupDTO {
    private Set<String> role;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    private String username;
    private String password;
    private String email;
}