package com.adda.service;

import com.adda.DTO.user.UserDTO;
import com.adda.domain.UserEntity;
import com.adda.exception.UserNotFoundException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.Objects;

public interface UserService {

    UserEntity registerUser(UserDTO signUpDto);

    UserEntity getOneUser(Long id) throws UserNotFoundException;

    List<UserEntity> getAll();

    Long delete(Long id);

    UserEntity encodeUserFromToken(String token);

    UserEntity findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    static String getBearerTokenHeader() {
        return ((ServletRequestAttributes) Objects.requireNonNull(
                RequestContextHolder.getRequestAttributes()))
                .getRequest()
                .getHeader("Authorization");
    }
}
