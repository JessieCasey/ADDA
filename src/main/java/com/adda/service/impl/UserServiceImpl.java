package com.adda.service.impl;

import com.adda.DTO.user.UserDTO;
import com.adda.DTO.user.UserDeletedDTO;
import com.adda.DTO.user.UserResponseDTO;
import com.adda.DTO.user.UserUpdateDTO;
import com.adda.domain.AdvertisementEntity;
import com.adda.domain.RoleEntity;
import com.adda.domain.UserEntity;
import com.adda.exception.NullEntityReferenceException;
import com.adda.repository.RoleRepository;
import com.adda.repository.UserRepository;
import com.adda.service.AdvertisementService;
import com.adda.service.UserService;
import com.adda.service.WishListService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final AdvertisementService advertisementService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final WishListService wishListService;

    @Lazy
    @Autowired
    public UserServiceImpl(AdvertisementService advertisementService, UserRepository userRepository,
                           RoleRepository roleRepository, PasswordEncoder passwordEncoder, WishListService wishListService) {
        this.advertisementService = advertisementService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.wishListService = wishListService;
    }

    @Override
    public UserEntity registerUser(UserDTO dto) {
        log.info("Method 'RegisterUser' " + dto.toString());
        UserEntity user = new UserEntity(
                dto.getFirstName(), dto.getLastName(),
                dto.getUsername(), passwordEncoder.encode(dto.getPassword()), dto.getEmail());

        user.setRoles(Collections.singleton(roleRepository.findByName("ROLE_ADMIN").orElseThrow(IllegalArgumentException::new)));
        UserEntity save = userRepository.save(user);

        log.info("Method 'RegisterUser': User is saved to DB " + user.getUsername());
        wishListService.createWishList(user);
        log.info("Method 'wishListService.createWishList()': User is updated in DB");
        return save;
    }

    @Override
    public UserEntity getOneUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User with id: '" + id + "' is not found"));
    }

    @Override
    public UserEntity update(UserUpdateDTO userDTO) {
        UserEntity user = null;
        if (userDTO != null) {
            user = getOneUser(userDTO.getId());
        }

        if (user == null) {
            throw new NullEntityReferenceException("User cannot be 'null'");
        } else {
            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            user.setEmail(userDTO.getEmail());
            user.setPassword(userDTO.getPassword());

            if (!user.getRoles().equals(userDTO.getRoles())) {
                user.setRoles(userDTO.getRoles());
            }

            return userRepository.save(user);
        }

    }

    @Override
    public List<UserEntity> getAll() {
        return userRepository.findAll();
    }

    @Override
    public UserDeletedDTO delete(Long id) {
        if (userRepository.existsById(id)) {
            List<AdvertisementEntity> allByUser = advertisementService.getAllByUser(id);
            UserDeletedDTO userDeletedDTO = new UserDeletedDTO(userRepository.getById(id), allByUser.size(), LocalDateTime.now());

            allByUser.forEach(x -> advertisementService.deleteAdvertById(x.getId()));
            userRepository.deleteById(id);

            log.info("Method 'delete(Long id)': User is deleted from the DB");
            return userDeletedDTO;
        } else {
            throw new EntityNotFoundException("User with id " + id + " not found");
        }
    }

    @Override
    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User with email: '" + email + "' is not found"));
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public UserEntity encodeUserFromToken(String token) {
        String[] chunks = token.split("\\.");

        Base64.Decoder decoder = Base64.getUrlDecoder();
        String payload = new String(decoder.decode(chunks[1]));

        Long id = new JSONObject(payload).getLong("id");

        if (userRepository.findById(id).isEmpty()) {
            throw new UsernameNotFoundException("You are not registered");
        }
        return userRepository.findById(id).get();
    }

}