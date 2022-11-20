package com.adda.user.dto;

import com.adda.user.User;
import com.adda.user.role.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * The UserResponseDTO class is required if we want to represent the user entity in flexible format.
 */
@Getter
@Setter
public class UserResponseDTO {
    private long id;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    private String username;
    private String email;
    private UUID wishList;
    private List<String> roles;

    public UserResponseDTO(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.wishList = user.getWishList();
        this.roles = user.getRoles().stream().map(x -> x.getName().toString()).collect(Collectors.toList());
    }
}
