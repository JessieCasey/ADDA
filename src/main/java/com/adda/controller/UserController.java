package com.adda.controller;

import com.adda.DTO.advertisements.AdvertResponseDTO;
import com.adda.DTO.user.UserResponseDTO;
import com.adda.DTO.user.UserUpdateDTO;
import com.adda.domain.UserEntity;
import com.adda.exception.UserNotFoundException;
import com.adda.service.HistoryService;
import com.adda.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.stream.Collectors;

import static com.adda.service.UserService.getBearerTokenHeader;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("api/user")
@Slf4j
public class UserController {

    private final UserService userService;
    private final HistoryService historyService;
    public UserController(UserService userService, HistoryService historyService) {
        this.userService = userService;
        this.historyService = historyService;
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> updateUser(@PathVariable Long id,
                                        @RequestBody UserUpdateDTO updateDTO) {
        log.info("[PUT] Request to update user");

        UserEntity user = userService.encodeUserFromToken(getBearerTokenHeader());

        if (!(user.getRoles().stream().anyMatch(o -> "ROLE_ADMIN".equals(o.getName())) || userService.getOneUser(id).equals(user))) {
            log.warn("[PUT] Request to 'updateUser': User is not an admin and is not the same user");
            return ResponseEntity.badRequest().body("You are not admin and not the owner of advert");
        }

        updateDTO.setId(id);
        UserEntity updated = userService.update(updateDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(updated.getId())
                .toUri();

        return ResponseEntity.created(location).body(new UserResponseDTO(updated));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        log.info("[Get] Request to method 'getUser'");
        try {
            return ResponseEntity.ok(new UserResponseDTO(userService.getOneUser(id)));
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
            return ResponseEntity.ok(userService.getAll().stream().map(UserResponseDTO::new).collect(Collectors.toList()));
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

    @GetMapping("/history/{id}")
    public ResponseEntity<?> getHistoryOfUser(@PathVariable Long id) {
        log.info("[Delete] Request to method 'deleteUser'");
        try {
            return ResponseEntity.ok(historyService.getUserHistory(id)
                    .getAdverts().stream().map(AdvertResponseDTO::new).collect(Collectors.toList()));
        } catch (Exception e) {
            log.error("Error in method 'deleteUser': " + e.getMessage());
            return ResponseEntity.badRequest().body("No user found");
        }
    }
}