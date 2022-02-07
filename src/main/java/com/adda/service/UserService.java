package com.adda.service;

import com.adda.domain.UserEntity;
import com.adda.exception.UserAlreadyExistException;
import com.adda.exception.UserNotFoundException;
import com.adda.model.User;
import com.adda.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}