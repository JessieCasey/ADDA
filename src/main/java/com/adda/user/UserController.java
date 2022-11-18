package com.adda.user;

import com.adda.advert.dto.AdvertResponseDTO;
import com.adda.advice.MessageException;
import com.adda.user.dto.UserResponseDTO;
import com.adda.user.dto.UserUpdateDTO;
import com.adda.user.exception.UserNotFoundException;
import com.adda.user.history.HistoryService;
import com.adda.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("api/users")
@Slf4j
public class UserController {

    private final UserService userService;
    private final HistoryService historyService;

    public UserController(UserService userService, HistoryService historyService) {
        this.userService = userService;
        this.historyService = historyService;
    }

    @PutMapping("/{id}")
    @PreAuthorize("#user.id == #id or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> updateUser(@PathVariable Long id,
                                        @RequestBody UserUpdateDTO updateDTO,
                                        @AuthenticationPrincipal UserDetails user,
                                        WebRequest request) {
        log.info("[PUT][UserController] Request to method 'updateUser'");
        try {
            updateDTO.setId(id);
            User updated = userService.update(updateDTO);

            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(updated.getId())
                    .toUri();

            return ResponseEntity.created(location).body(new UserResponseDTO(updated));
        } catch (Exception e) {
            log.error("Error in method 'updateUser': " + e.getMessage());
            return ResponseEntity.badRequest().body(new MessageException(e.getMessage(), request));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id, WebRequest request) {
        log.info("[Get] Request to method 'getUser'");
        try {
            return ResponseEntity.ok(new UserResponseDTO(userService.getOneUser(id)));
        } catch (Exception e) {
            log.error("Error in method 'getUser': " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageException(HttpStatus.BAD_REQUEST.value(), e.getMessage(), request));
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
    @PreAuthorize("#user.id == #id or hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long id,
                                        @AuthenticationPrincipal UserDetails user) {
        log.info("[Delete] Request to method 'deleteUser'");
        try {
            return ResponseEntity.ok(userService.delete(id));
        } catch (Exception e) {
            log.error("Error in method 'deleteUser': " + e.getMessage());
            return ResponseEntity.badRequest().body("No user found");
        }
    }

    @GetMapping("/history/{id}")
    @PreAuthorize("#user.id == #id or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> getHistoryOfUser(@PathVariable Long id,
                                              @AuthenticationPrincipal UserDetails user) {
        log.info("[GET] Request to method 'getHistoryOfUser'");
        try {
            List<AdvertResponseDTO> collect = historyService.getUserHistory(id).stream().map(AdvertResponseDTO::new).collect(Collectors.toList());
            return ResponseEntity.ok(collect.size() == 0 ? "User hasn't visited any adverts yet" : collect);
        } catch (Exception e) {
            log.error("Error in method 'getHistoryOfUser': " + e.getMessage());
            return ResponseEntity.badRequest().body("No user found");
        }
    }
}