package com.adda.service;

import com.adda.DTO.UserDTO;
import com.adda.domain.RoleEntity;
import com.adda.domain.UserEntity;
import com.adda.exception.UserNotFoundException;
import com.adda.model.User;
import com.adda.repository.RoleRepository;
import com.adda.repository.UserRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Collections;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public ResponseEntity<?> registerUser(UserDTO signUpDto) {

        // add check for email exists in DB
        if (userRepository.existsByEmail(signUpDto.getEmail())) {
            return new ResponseEntity<>("Email is already taken!", HttpStatus.BAD_REQUEST);
        }

        // create user object
        UserEntity user = new UserEntity();
        user.setId(signUpDto.getId());
        user.setFirstName(signUpDto.getFirstName());
        user.setLastName(signUpDto.getLastName());
        user.setUsername(signUpDto.getUsername());
        user.setEmail((signUpDto.getEmail()));

        RoleEntity roles = roleRepository.findByName("ROLE_ADMIN").get();
        user.setRoles(Collections.singleton(roles));

        userRepository.save(user);
        return new ResponseEntity<>("Done", HttpStatus.OK);
    }


    public User getOneUser(Long id) throws UserNotFoundException {
        UserEntity user = userRepository.findById(id).get();
        if (user == null) {
            throw new UserNotFoundException("Пользователь не найден");
        }
        return User.toModel(user);
    }

    public Iterable<UserEntity> getAll() {
        Iterable<UserEntity> userEntities = userRepository.findAll();
        return userEntities;
    }

    public Long delete(Long id) {
        userRepository.deleteById(id);
        return id;
    }

    public UserEntity encodeUserFromToken(String token) throws Exception {
        String[] chunks;
        if (!token.contains(" ")) {
            chunks = token.split("\\.");
        } else {
            chunks = token.substring(token.indexOf(" ")).split("\\.");
        }
        Base64.Decoder decoder = Base64.getUrlDecoder();

        String header = new String(decoder.decode(chunks[0]));
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