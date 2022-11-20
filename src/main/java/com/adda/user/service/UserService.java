package com.adda.user.service;

import com.adda.user.User;
import com.adda.user.dto.UserDeletedDTO;
import com.adda.user.dto.UserUpdateDTO;
import com.adda.user.role.Role;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;

/**
 * The UserService interface is required to create UserServiceImpl {@link UserServiceImpl}
 */

public interface UserService {

    //  Fetch data
    Page<User> fetchUserDataAsPageWithFilteringAndSorting(String firstNameFilter, String lastNameFilter, int page, int size, List<String> sortList, String toString);

    Page<User> fetchUserDataAsPageWithFiltering(String firstNameFilter, String lastNameFilter, int page, int size);

    List<User> fetchFilteredUserDataAsList(String firstNameFilter, String lastNameFilter);

    List<User> fetchUserDataAsList();

    User getOneUser(Long id);

    //  Updating and verifying user's credentials
    User update(UserUpdateDTO updateDTO);

    void updatePassword(String token);

    void updatePasswordById(Long userId, String newPassword);

    void sendVerification(Long id, String newPassword);

    String verify(String code);

    UserDeletedDTO delete(Long id);

    Set<Role> getRolesList(Set<String> strRoles);
}
