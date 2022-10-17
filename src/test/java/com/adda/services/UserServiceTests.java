package com.adda.services;

import com.adda.DTO.user.UserDTO;
import com.adda.DTO.user.UserUpdateDTO;
import com.adda.domain.RoleEntity;
import com.adda.domain.UserEntity;
import com.adda.exception.NullEntityReferenceException;
import com.adda.exception.UserNotFoundException;
import com.adda.repository.RoleRepository;
import com.adda.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
public class UserServiceTests {

    private final UserService userService;
    private final RoleRepository roleRepository;

    @Autowired
    public UserServiceTests(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @Test
    @Transactional
    public void createUserTest() {
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName("Tony");
        userDTO.setLastName("Show");
        userDTO.setEmail("validEmail@gmail.com");
        userDTO.setPassword("12345Q");
        userDTO.setUsername("TonyHeritage");

        UserEntity user = userService.registerUser(userDTO);
        assertEquals("TonyHeritage", user.getUsername());
    }

    @Test
    public void readUserByIdTest() throws UserNotFoundException {
        UserEntity actual = userService.getOneUser(1L);
        assertEquals(1, actual.getId());
    }

    @Test
    public void readUserByIdIsNotFoundTest() {
        long testId = 100L;
        Throwable t = Assertions.assertThrows(UsernameNotFoundException.class,
                () -> userService.getOneUser(testId));

        Assertions.assertEquals("User with id: '" + testId + "' is not found", t.getMessage());
    }

    @Test
    @Transactional
    public void updateUserTest() {
        String expected = "Peter";
        RoleEntity expectedRole = roleRepository.findByName("ROLE_USER").get();
        RoleEntity expectedRoleAdmin = roleRepository.findByName("ROLE_ADMIN").get();
        UserEntity user = userService.getOneUser(1L);
        UserUpdateDTO updateDTO = new UserUpdateDTO(user);
        updateDTO.getRoles().add(expectedRoleAdmin);
        updateDTO.getRoles().add(expectedRole);
        updateDTO.setFirstName("Peter");
        UserEntity actual = userService.update(updateDTO);

        assertEquals(expected, actual.getFirstName());
        assertTrue(actual.getRoles().contains(expectedRole));
        assertTrue(actual.getRoles().contains(expectedRoleAdmin));
    }

    @Test
    public void exceptionWhenUpdateNullUserTest() {
        Throwable t = Assertions.assertThrows(NullEntityReferenceException.class,
                () -> userService.update(null));

        Assertions.assertEquals("User cannot be 'null'", t.getMessage());
    }

    @Test
    @Transactional
    public void deleteUserTest() {
        int expected = userService.getAll().size() - 1;
        userService.delete(1L);
        List<UserEntity> users = userService.getAll();
        assertEquals(expected, users.size());
    }

    @Test
    public void exceptionWhenDeleteUserWithNonExistingIdTest() {
        long testId = 100L;
        Throwable t = Assertions.assertThrows(EntityNotFoundException.class,
                () -> userService.delete(testId));

        Assertions.assertEquals("User with id " + testId + " not found", t.getMessage());
    }
}