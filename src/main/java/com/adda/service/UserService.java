package com.adda.service;

import com.adda.DTO.UserDTO;
import com.adda.domain.UserEntity;
import com.adda.exception.UserNotFoundException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

public interface UserService {

    void registerUser(UserDTO signUpDto);

    UserEntity getOneUser(Long id) throws UserNotFoundException;

    Iterable<UserEntity> getAll();

    Long delete(Long id);

    UserEntity encodeUserFromToken(String token) throws Exception;

    static String getBearerTokenHeader() {
        return ((ServletRequestAttributes) Objects.requireNonNull(
                RequestContextHolder.getRequestAttributes()))
                .getRequest()
                .getHeader("Authorization");
    }
}
