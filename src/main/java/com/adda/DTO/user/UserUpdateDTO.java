package com.adda.DTO.user;

import com.adda.domain.RoleEntity;
import com.adda.domain.UserEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.management.relation.Role;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class UserUpdateDTO {
    private Long Id;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    private String username;
    private String password;
    private String email;
    private Set<RoleEntity> roles;

    public UserUpdateDTO(UserEntity user) {
        Id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.roles = user.getRoles();
    }
}
