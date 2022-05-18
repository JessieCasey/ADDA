package com.adda.service;

import com.adda.domain.UserEntity;
import com.adda.exception.UserAlreadyExistException;
import com.adda.exception.UserNotFoundException;
import com.adda.model.User;
import com.adda.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Base64;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    public UserEntity registration(UserEntity user) throws UserAlreadyExistException {
        if (userRepo.findByUsername(user.getUsername()) != null) {
            throw new UserAlreadyExistException("Пользователь с таким именем существует");
        }
        return userRepo.save(user);
    }

    public User getOneUser(Long id) throws UserNotFoundException {
        UserEntity user = userRepo.findById(id).get();
        if (user == null) {
            throw new UserNotFoundException("Пользователь не найден");
        }
        return User.toModel(user);
    }

    public Iterable<UserEntity> getAll() {
        Iterable<UserEntity> userEntities = userRepo.findAll();
        return userEntities;
    }

    public Long delete(Long id) {
        userRepo.deleteById(id);
        return id;
    }

    public static ResponseEntity<String> encodeToken(String token) {
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();

        String header = new String(decoder.decode(chunks[0]));
        String payload = new String(decoder.decode(chunks[1]));

        return new ResponseEntity<>(header + "\n" + payload, HttpStatus.OK);
    }


}