package com.adda.service.impl;

import com.adda.DTO.UserDTO;
import com.adda.domain.UserEntity;
import com.adda.exception.UserNotFoundException;
import com.adda.repository.RoleRepository;
import com.adda.repository.UserRepository;
import com.adda.service.UserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Lazy
    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void registerUser(UserDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            new ResponseEntity<>("Email is already taken!", HttpStatus.BAD_REQUEST);
            return;
        }

        UserEntity user = new UserEntity(
                dto.getFirstName(), dto.getLastName(),
                dto.getUsername(), passwordEncoder.encode(dto.getPassword()), dto.getEmail());

        user.setRoles(Collections.singleton(roleRepository.findByName("ROLE_ADMIN").orElseThrow(IllegalArgumentException::new)));
        userRepository.save(user);
    }


    @Override
    public UserEntity getOneUser(Long id) throws UserNotFoundException {
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
    public UserEntity encodeUserFromToken(String token) {
        String[] chunks = token.split("\\.");

        Base64.Decoder decoder = Base64.getUrlDecoder();
        String payload = new String(decoder.decode(chunks[1]));

        JSONObject obj = new JSONObject(payload);
        Long id = obj.getLong("id");

        if (!userRepository.findById(id).isPresent()) {
            throw new IllegalArgumentException("You are not registered");
        }
        return userRepository.findById(id).get();
    }

}