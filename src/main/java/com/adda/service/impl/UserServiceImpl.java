package com.adda.service.impl;

import com.adda.DTO.user.UserDTO;
import com.adda.domain.UserEntity;
import com.adda.repository.RoleRepository;
import com.adda.repository.UserRepository;
import com.adda.service.UserService;
import com.adda.service.WishListService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Collections;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final WishListService wishListService;
    @Lazy
    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, WishListService wishListService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.wishListService = wishListService;
    }

    @Override
    public UserEntity registerUser(UserDTO dto) {
        UserEntity user = new UserEntity(
                dto.getFirstName(), dto.getLastName(),
                dto.getUsername(), passwordEncoder.encode(dto.getPassword()), dto.getEmail());
        user.setRoles(Collections.singleton(roleRepository.findByName("ROLE_ADMIN").orElseThrow(IllegalArgumentException::new)));
        UserEntity save = userRepository.save(user);
        wishListService.createWishList(user);
        return save;
    }


    @Override
    public UserEntity getOneUser(Long id) {
        return userRepository.findById(id).orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public List<UserEntity> getAll() {
        return userRepository.findAll();
    }

    @Override
    public Long delete(Long id) {
        userRepository.deleteById(id);
        return id;
    }

    @Override
    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User doesn't exists"));
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

        JSONObject obj = new JSONObject(payload);
        Long id = obj.getLong("id");

        if (userRepository.findById(id).isEmpty()) {
            throw new IllegalArgumentException("You are not registered");
        }
        return userRepository.findById(id).get();
    }

}