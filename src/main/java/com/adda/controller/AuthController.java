package com.adda.controller;

import com.adda.DTO.AuthenticationRequestDto;
import com.adda.DTO.user.UserDTO;
import com.adda.DTO.user.UserResponseDTO;
import com.adda.config.security.JwtTokenProvider;
import com.adda.domain.UserEntity;
import com.adda.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @Lazy
    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequestDto request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()));

            UserEntity user = userService.findByEmail(request.getEmail());

            String token = jwtTokenProvider.createToken(user.getId(), request.getEmail(), user.getRoles().stream().findFirst().get().getName());
            Map<Object, Object> response = new HashMap<>();
            response.put("email", request.getEmail());
            response.put("token", token);
            response.put("userId", user.getId());

            return ResponseEntity.ok(response);

        } catch (AuthenticationException e) {
            return new ResponseEntity<>("Invalid email / password combination", HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDTO dto) {
        log.info("[Post] Request to method 'registerUser'");
        if (userService.existsByEmail(dto.getEmail()) || userService.existsByUsername(dto.getUsername())) {
            log.warn("Warning in method 'registerUser': Email or Username is already taken!");
            return new ResponseEntity<>("Email or Username is already taken!", HttpStatus.BAD_REQUEST);
        }

        UserEntity userEntity = userService.registerUser(dto);

        return new ResponseEntity<>(new UserResponseDTO(userEntity), HttpStatus.OK);
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, response, null);
    }
}

