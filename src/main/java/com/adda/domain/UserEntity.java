package com.adda.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "users_table", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"username"}),
        @UniqueConstraint(columnNames = {"email"})
})
public class UserEntity {
    @Id
    private long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private UUID wishList;


    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<RoleEntity> roles;

    public UserEntity(String firstName, String lastName, String username, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
    }

    public UserEntity(long id, String firstName, String lastName, String username, String email,
                      Set<RoleEntity> roles, UUID wishList) {
        this(firstName, lastName, username, email);
        this.id = id;
        this.roles = roles;
        this.wishList = wishList;
    }

    public UserEntity() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return id == that.id && Objects.equals(firstName, that.firstName)
                && Objects.equals(lastName, that.lastName)
                && Objects.equals(username, that.username)
                && Objects.equals(roles, that.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, username, roles);
    }
}