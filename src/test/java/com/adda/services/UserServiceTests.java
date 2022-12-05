package com.adda.services;

import com.adda.AddaApplication;
import com.adda.advert.repository.AdvertRepository;
import com.adda.advert.service.AdvertService;
import com.adda.user.User;
import com.adda.user.dto.UserDeletedDTO;
import com.adda.user.dto.UserUpdateDTO;
import com.adda.user.repository.UserRepository;
import com.adda.user.role.ERole;
import com.adda.user.role.Role;
import com.adda.user.role.RoleRepository;
import com.adda.user.service.UserService;
import com.adda.user.updateToken.UpdateToken;
import com.adda.user.updateToken.UpdateTokenService;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AddaApplication.class)
public class UserServiceTests {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AdvertRepository advertRepository;

    @Autowired
    private UpdateTokenService updateTokenService;

    @Test
    public void fetchUserDataAsPageWithFilteringAndSorting() {
        List<User> expected = userRepository.findAll().stream()
                .sorted((o1, o2) -> o2.getEmail().compareTo(o1.getEmail()))
                .limit(2)
                .collect(Collectors.toList());

        Page<User> actual = userService.
                fetchUserDataAsPageWithFilteringAndSorting("", "", 0, 2, new ArrayList<>(List.of("email")), "DESC");

        assertEquals(expected.get(1), actual.getContent().get(1));
    }

    @Test
    public void update() {
        User before = userService.getOneUser(2L);
        UserUpdateDTO toBeUpdated = new UserUpdateDTO();
        toBeUpdated.setId(2L);
        toBeUpdated.setEmail("newemail@gmail.com");
        toBeUpdated.setFirstName("Newname");
        toBeUpdated.setLastName("Newlast");

        Set<String> stringStr = new HashSet<>();
        stringStr.add("admin");
        stringStr.add("mod");
        stringStr.add("user");

        toBeUpdated.setRoles(stringStr);

        User after = userService.update(toBeUpdated);

        assertEquals(before.getId(), after.getId());
        assertEquals(before.getUsername(), after.getUsername());
        assertNotEquals(before.getFirstName(), after.getLastName());
        assertNotEquals(before.getLastName(), after.getLastName());
        assertNotEquals(before.getRoles().size(), after.getRoles().size());
        assertNotEquals(before.getEmail(), after.getEmail());
    }

    @Test
    public void fetchUserDataAsPageWithFiltering() {
        Page<User> expected = userRepository.findByFirstNameLikeAndLastNameLike("Al", "Wh", PageRequest.of(0, 2));
        Page<User> actual = userService.fetchUserDataAsPageWithFiltering("Al", "Wh", 0, 2);

        assertEquals(expected.getContent(), actual.getContent());
        assertEquals(expected.getTotalPages(), actual.getTotalPages());
        assertEquals(expected.getTotalElements(), actual.getTotalElements());
    }

    @Test
    public void getRolesList() {
        Set<Role> expected = new HashSet<>();
        expected.add(roleRepository.findByName(ERole.ROLE_ADMIN).get());
        expected.add(roleRepository.findByName(ERole.ROLE_USER).get());
        expected.add(roleRepository.findByName(ERole.ROLE_MODERATOR).get());

        Set<String> stringStr = new HashSet<>();
        stringStr.add("admin");
        stringStr.add("mod");
        stringStr.add("user");
        Set<Role> actual = userService.getRolesList(stringStr);
        assertEquals(expected.size(), actual.size());

        Set<Role> expected2 = new HashSet<>();
        expected2.add(roleRepository.findByName(ERole.ROLE_USER).get());
        assertEquals(expected2.size(), userService.getRolesList(null).size());
    }

    @Test
    public void getOneUser() {
        long testId = 100L;
        Throwable t = Assertions.assertThrows(UsernameNotFoundException.class,
                () -> userService.getOneUser(100L));

        Assertions.assertEquals("User with id: '" + testId + "' is not found", t.getMessage());
    }

    @Test
    @Transactional
    public void deleteUserFromDB() {
        UserDeletedDTO delete = userService.delete(2L);
        assertFalse(userRepository.existsById(2L));
        assertEquals(2, delete.getDeletedAdverts());
        assertFalse(advertRepository.existsById(UUID.fromString("e0bbdb63-8cbc-49aa-a442-b7ba6ca20e86")));
        assertFalse(advertRepository.existsById(UUID.fromString("46ef9821-5f1f-4927-a98f-1a94f71703eb")));
    }

    @Test
    public void updateUserPassword() {
        UpdateToken updateToken = updateTokenService.createUpdateToken(3L, "jasnd21mda");
        User before = userRepository.findById(3L).get();
        userService.updatePassword(updateToken.getToken());
        User after = userRepository.findById(3L).get();
        assertNotEquals(before.getPassword(), after.getPassword());
    }

    @Test
    public void updateUserPasswordByID() {
        User before = userRepository.findById(3L).get();
        userService.updatePasswordById(3L, "ghfhdsjkm123");
        User after = userRepository.findById(3L).get();
        assertNotEquals(before.getPassword(), after.getPassword());
    }
}