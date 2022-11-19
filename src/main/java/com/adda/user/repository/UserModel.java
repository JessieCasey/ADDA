package com.adda.user.repository;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.util.UUID;

/**
 * The UserModel class extends the Hateoas Representation Model and is required if we want to convert the User
 * Entity to a pagination format
 */
@Getter
@Setter
public class UserModel extends RepresentationModel<UserModel> {
    private long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private UUID wishList;
}