package com.adda.controller;

import com.adda.DTO.LoginDTO;
import com.adda.DTO.UserDTO;
import com.adda.domain.RoleEntity;
import com.adda.domain.UserEntity;
import com.adda.repository.RoleRepository;
import com.adda.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @PostMapping("/signin")
    public ResponseEntity<String> authenticateUser(@RequestBody LoginDTO loginDto) {
        log.info("[Post] Request to method 'authenticateUser'");
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsernameOrEmail(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new ResponseEntity<>("User signed-in successfully!.", HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO dto) {
        log.info("[Post] Request to method 'registerUser'");
        if (userRepository.existsByEmail(dto.getEmail())) {
            log.warn("Warning in method 'registerUser': email is already taken!");
            return new ResponseEntity<>("Email is already taken!", HttpStatus.BAD_REQUEST);
        }

        UserEntity user = new UserEntity(dto.getFirstName(), dto.getLastName(), dto.getUsername(), dto.getEmail());

        RoleEntity roles = roleRepository.findByName("ROLE_ADMIN").orElseThrow(IllegalArgumentException::new);
        user.setRoles(Collections.singleton(roles));

        userRepository.save(user);
        return new ResponseEntity<>("User is registered successfully", HttpStatus.OK);

    }
}

