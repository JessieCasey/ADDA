package com.adda.user;

import com.adda.user.dto.UserDTO;
import com.adda.user.dto.UserDeletedDTO;
import com.adda.user.dto.UserUpdateDTO;
import com.adda.user.exception.UserNotFoundException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.Objects;

public interface UserService {

    UserEntity registerUser(UserDTO signUpDto);

    UserEntity getOneUser(Long id) throws UserNotFoundException;

    UserEntity update(UserUpdateDTO user);

    List<UserEntity> getAll();

    UserDeletedDTO delete(Long id);

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
