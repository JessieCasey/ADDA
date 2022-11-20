package com.adda.email;

import com.adda.auth.dto.SignInDTO;
import com.adda.auth.jwt.JwtResponse;
import com.adda.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/verify")
@Slf4j
public class EmailController {

    private final UserService userService;

    /**
     * Constructor for {@link EmailController}.
     *
     * @param userService {@link UserService}
     */
    public EmailController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Method that verifying user after registration. {@link com.adda.user.User}
     *
     * @param code verification code.
     * @return ResponseEntity<JwtResponse> object in case of success. {@link ResponseEntity}
     *
     * @author Artem Komarov
     */
    @GetMapping
    public ResponseEntity<?> verifyUser(@RequestParam String code) {
        log.info("[GET][EmailController]: method 'verifyUser'");
        try {
            String verify = userService.verify(code);
            if (!verify.isEmpty()) {
                return ResponseEntity.ok("Verified");
            } else {
                return ResponseEntity.badRequest().body("Not verified");
            }
        } catch (Exception e) {
            log.error("[GET][EmailController]: Error method 'verifyUser': " + e.getMessage());
            return ResponseEntity.badRequest().body(e);
        }
    }
}