package com.adda.user.service;

import com.adda.auth.dto.SignInDTO;
import com.adda.auth.dto.SignupDTO;
import com.adda.auth.jwt.JwtResponse;
import com.adda.user.User;
import com.adda.user.dto.UserDeletedDTO;
import com.adda.user.dto.UserUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.Objects;

public interface UserService {

    static String getBearerTokenHeader() {
        return ((ServletRequestAttributes) Objects.requireNonNull(
                RequestContextHolder.getRequestAttributes()))
                .getRequest()
                .getHeader("Authorization");
    }

    JwtResponse authenticate(SignInDTO loginRequest);

    void createUser(SignupDTO signUpRequest, String siteURL);

    User encodeUserFromToken(String bearerTokenHeader);

    User update(UserUpdateDTO updateDTO);

    User getOneUser(Long id);

    UserDeletedDTO delete(Long id);

    List<User> getAll();

    String verify(String code);

    Page<User> fetchCustomerDataAsPageWithFilteringAndSorting(String firstNameFilter, String lastNameFilter, int page, int size, List<String> sortList, String toString);
}
