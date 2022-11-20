package com.adda.user.service;

import com.adda.advert.Advert;
import com.adda.advert.service.AdvertService;
import com.adda.email.EmailService;
import com.adda.exception.NullEntityReferenceException;
import com.adda.url.service.UrlService;
import com.adda.user.User;
import com.adda.user.dto.UserDeletedDTO;
import com.adda.user.dto.UserUpdateDTO;
import com.adda.user.exception.UserNotFoundException;
import com.adda.user.repository.UserRepository;
import com.adda.user.role.ERole;
import com.adda.user.role.Role;
import com.adda.user.role.RoleRepository;
import com.adda.user.updateToken.UpdateToken;
import com.adda.user.updateToken.UpdateTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The UserServiceImpl class implements UserService interface to create methods {@link UserService}
 */

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final AdvertService advertService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final UpdateTokenService updateTokenService;

    @Autowired
    public UserServiceImpl(AdvertService advertService, UserRepository userRepository,
                           RoleRepository roleRepository, PasswordEncoder passwordEncoder,
                           EmailService emailService, UpdateTokenService updateTokenService) {
        this.advertService = advertService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.updateTokenService = updateTokenService;
    }

    @Override
    public Page<User> fetchUserDataAsPageWithFilteringAndSorting(String firstNameFilter, String lastNameFilter, int page, int size, List<String> sortList, String sortOrder) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(createSortOrder(sortList, sortOrder)));
        return userRepository.findByFirstNameLikeAndLastNameLike(firstNameFilter, lastNameFilter, pageable);
    }

    @Override
    public List<User> fetchFilteredUserDataAsList(String firstNameFilter, String lastNameFilter) {
        return userRepository.findByFirstNameLikeAndLastNameLike(firstNameFilter, lastNameFilter);
    }

    @Override
    public Page<User> fetchUserDataAsPageWithFiltering(String firstNameFilter, String lastNameFilter, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findByFirstNameLikeAndLastNameLike(firstNameFilter, lastNameFilter, pageable);
    }

    @Override
    public List<User> fetchUserDataAsList() {
        return userRepository.findAll();
    }

    public List<Sort.Order> createSortOrder(List<String> sortList, String sortDirection) {
        List<Sort.Order> sorts = new ArrayList<>();
        Sort.Direction direction;
        for (String sort : sortList) {
            if (sortDirection != null) {
                direction = Sort.Direction.fromString(sortDirection);
            } else {
                direction = Sort.Direction.DESC;
            }
            sorts.add(new Sort.Order(direction, sort));
        }
        return sorts;
    }

    @Override
    public Set<Role> getRolesList(Set<String> strRoles) {
        Set<Role> roles = new HashSet<>();
        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin" -> {
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                    }
                    case "mod" -> {
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);
                    }
                    default -> {
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                    }
                }
            });
        }
        return roles;
    }

    @Override
    public User getOneUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User with id: '" + id + "' is not found"));
    }

    @Override
    public User update(UserUpdateDTO userDTO) {
        User user = null;
        if (userDTO != null) {
            user = getOneUser(userDTO.getId());
        }

        if (user == null) {
            throw new NullEntityReferenceException("User cannot be 'null'");
        } else {
            if (userDTO.getFirstName() != null)
                user.setFirstName(userDTO.getFirstName());
            if (userDTO.getLastName() != null)
                user.setLastName(userDTO.getLastName());
            if (userDTO.getEmail() != null)
                user.setEmail(userDTO.getEmail());
            if (userDTO.getRoles() != null && userDTO.getRoles().size() != 0)
                user.setRoles(getRolesList(userDTO.getRoles()));

            return userRepository.save(user);
        }
    }

    @Override
    public UserDeletedDTO delete(Long id) {
        if (userRepository.existsById(id)) {
            List<Advert> allByUser = advertService.getAllByUser(id);
            UserDeletedDTO userDeletedDTO =
                    new UserDeletedDTO(userRepository.getById(id), allByUser.size(), LocalDateTime.now());

            allByUser.forEach(x -> advertService.deleteAdvertById(x.getId()));
            userRepository.deleteById(id);

            log.info("Method 'delete()': User is deleted from the DB");
            return userDeletedDTO;
        } else {
            throw new UserNotFoundException("User with id '" + id + "' is not found");
        }
    }

    @Override
    public String verify(String verificationCode) {
        log.info("Method 'verify': method is invoked");
        User user = userRepository.findByVerificationCode(verificationCode).orElseThrow();

        if (user.isEnabled()) {
            log.warn("Method 'verify': User is already enabled or null");
            return "Already done";
        } else {
            log.info("Method 'verify': User '" + user.getUsername() + "' is verified");
            user.setVerificationCode(null);
            user.setEnabled(true);
            userRepository.save(user);

            String content = "Hi again! " + user.getUsername() + ",<br>"
                    + "You've completed registration, <br>"
                    + "Feel free to start you business with us, <br>"
                    + "ADDA - the best market place";

            emailService.sendEmail(user.getEmail(), content, "Registration completed");
            log.info("Method 'verify': Successful");
            return "Done";
        }
    }

    public void updatePassword(String token) {
        UpdateToken updateToken = updateTokenService.getById(token);
        User user = userRepository.getById(updateToken.getUser().getId());
        user.setPassword(passwordEncoder.encode(updateToken.getSensitiveData()));
        userRepository.save(user);

    }

    public void updatePasswordById(Long userId, String password) {
        User user = userRepository.getById(userId);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    public void sendVerification(Long id, String newPassword) {
        User user = userRepository.getById(id);
        UpdateToken updateToken = updateTokenService.createUpdateToken(id, newPassword);

        String content = "Hi again! " + user.getUsername() + ",<br>"
                + "You wanted to change your password, <br>"
                + "Please follow this link to update password: \n" +
                "<a href=\"http://localhost:8080/security/verify/" + updateToken.getToken() + "\" target=\"_self\">Change password</a>" +
                "\n Note! This code is valid for " + updateToken.getExpiryDate() +
                "\n ADDA - the best market place";

        emailService.sendEmail(user.getEmail(), content, "Update password");
    }
}