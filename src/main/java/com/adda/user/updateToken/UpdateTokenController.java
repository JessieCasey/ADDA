package com.adda.user.updateToken;

import com.adda.advice.MessageException;
import com.adda.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

@RestController
@RequestMapping("security")
@Slf4j
public class UpdateTokenController {
    private final UserService userService;

    public UpdateTokenController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/verify/{token}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> updateUserPassword(@PathVariable String token, WebRequest request) {
        log.info("[GET][UpdateTokenController] Request to method 'updateUser'");
        try {
            userService.updatePassword(token);
            return ResponseEntity.ok().body("Updated password");
        } catch (Exception e) {
            log.error("Error in method 'updateUser': " + e.getMessage());
            return ResponseEntity.badRequest().body(new MessageException(e.getMessage(), request));
        }
    }

    @PutMapping("/password/{userId}")
    @PreAuthorize("#user.id == #userId or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> updateUserPassword(@PathVariable Long userId,
                                                @RequestParam String newPassword,
                                                @AuthenticationPrincipal UserDetails user, WebRequest request) {
        log.info("[PUT][UpdateTokenController] Request to method 'updateUser'");
        try {

            if (request.isUserInRole("ROLE_ADMIN")) {
                log.info("[PUT][UpdateTokenController]: Admin is updating the password");
                userService.updatePasswordById(userId, newPassword);
                return ResponseEntity.ok().body("Password is updated by Admin");
            } else {
                userService.sendVerification(userId, newPassword);
                return ResponseEntity.ok().body("Follow the link");
            }

        } catch (Exception e) {
            log.error("Error in method 'updateUser': " + e.getMessage());
            return ResponseEntity.badRequest().body(new MessageException(e.getMessage(), request));
        }
    }
}
