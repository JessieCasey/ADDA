package com.adda.auth;

import com.adda.user.UserEntity;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Data
public class SecurityUser implements UserDetails {
    private long id;
    private String username;
    private String password;
    private boolean isActive;
    private Collection<? extends GrantedAuthority> roles;

    public SecurityUser(long id, String username,
                        String password, boolean isActive,
                        Collection<? extends GrantedAuthority> roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.isActive = isActive;
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isActive;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isActive;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isActive;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }

    public long getId() {
        return id;
    }

    public static UserDetails fromUser(UserEntity user) {
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(user.getRoles().stream().findFirst().get().getName()));

        return new SecurityUser(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                true,
                grantedAuthorities
        );
    }
}