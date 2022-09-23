package com.adda.DTO.user;

import com.adda.domain.RoleEntity;
import com.adda.domain.UserEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
public class UserResponseDTO {
    private long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private UUID wishList;
    private List<String> roles;

    public UserResponseDTO(UserEntity user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.wishList = user.getWishList();
        this.roles = user.getRoles().stream().map(RoleEntity::getName).collect(Collectors.toList());
    }
}
