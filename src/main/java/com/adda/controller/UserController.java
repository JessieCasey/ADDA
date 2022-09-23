package com.adda.controller;

import com.adda.exception.UserNotFoundException;
import com.adda.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("api/user")
@Slf4j
public class UserController {

    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        log.info("[Get] Request to method 'getUser'");
        try {
            return ResponseEntity.ok(userService.getOneUser(id));
        } catch (UserNotFoundException e) {
            log.error("Error type 'UserNotFoundException' in method 'getUser': " + e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("Error in method 'getUser': " + e.getMessage());
            return ResponseEntity.badRequest().body("No user found");
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        log.info("[Get] Request to method 'getAllUsers'");
        try {
            return ResponseEntity.ok(userService.getAll());
        } catch (Exception e) {
            log.error("Error in method 'getAllUsers': " + e.getMessage());
            return ResponseEntity.badRequest().body("You cannot get all users");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        log.info("[Delete] Request to method 'deleteUser'");
        try {
            return ResponseEntity.ok(userService.delete(id));
        } catch (Exception e) {
            log.error("Error in method 'deleteUser': " + e.getMessage());
            return ResponseEntity.badRequest().body("No user found");
        }
    }
}