package com.adda.service.impl;

import com.adda.DTO.UserDTO;
import com.adda.domain.RoleEntity;
import com.adda.domain.UserEntity;
import com.adda.exception.UserNotFoundException;
import com.adda.repository.RoleRepository;
import com.adda.repository.UserRepository;
import com.adda.service.UserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Collections;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public void registerUser(UserDTO signUpDto) {

        // add check for email exists in DB
        if (userRepository.existsByEmail(signUpDto.getEmail())) {
            new ResponseEntity<>("Email is already taken!", HttpStatus.BAD_REQUEST);
            return;
        }

        // create user object
        UserEntity user = new UserEntity();
        user.setId(signUpDto.getId());
        user.setFirstName(signUpDto.getFirstName());
        user.setLastName(signUpDto.getLastName());
        user.setUsername(signUpDto.getUsername());
        user.setEmail((signUpDto.getEmail()));

        System.out.println(roleRepository.findAll());
        RoleEntity roles = roleRepository.findByName("ROLE_ADMIN").orElseThrow(IllegalArgumentException::new);
        user.setRoles(Collections.singleton(roles));

        userRepository.save(user);

        new ResponseEntity<>("Done", HttpStatus.OK);
    }


    @Override
    public UserEntity getOneUser(Long id) throws UserNotFoundException {
        UserEntity user = userRepository.findById(id).get();
        if (user == null) {
            throw new UserNotFoundException("User is not found");
        }
        return user;
    }

    @Override
    public Iterable<UserEntity> getAll() {
        return userRepository.findAll();
    }

    @Override
    public Long delete(Long id) {
        userRepository.deleteById(id);
        return id;
    }

    @Override
    public UserEntity encodeUserFromToken(String token) throws Exception {
        String[] chunks;
        if (!token.contains(" ")) {
            chunks = token.split("\\.");
        } else {
            chunks = token.substring(token.indexOf(" ")).trim().split("\\.");
        }

        Base64.Decoder decoder = Base64.getUrlDecoder();
        String payload = new String(decoder.decode(chunks[1]));

        JSONObject obj = new JSONObject(payload);
        Long id = obj.getLong("user_id");
        String firstName = obj.getString("first_name");
        String lastName = obj.getString("last_name");
        String username = obj.getString("username");
        String email = obj.getString("email");

        if (!userRepository.findById(id).isPresent()) {
            registerUser(new UserDTO(id, firstName, lastName, username, email));
        }
        return userRepository.findById(id).get();
    }

}