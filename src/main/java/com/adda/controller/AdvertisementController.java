package com.adda.controller;

import com.adda.domain.AdvertisementEntity;
import com.adda.domain.UserEntity;
import com.adda.exception.AdvertisementNotFoundException;
import com.adda.repository.UserRepository;
import com.adda.service.AdvertisementService;
import com.adda.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("api/adv")
public class AdvertisementController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private AdvertisementService advertisementService;

    @PostMapping("/")
    public HttpEntity<String> addAdvertisement(@RequestBody AdvertisementEntity advertisement, Principal authUser) {
        try {
            UserEntity user = userRepository.findByUsernameOrEmail(authUser.getName(), authUser.getName())
                    .orElseThrow(() ->
                            new UsernameNotFoundException("User not found with username or email:" + authUser.getName()));

            advertisementService.addAdvert(advertisement, user);
            return ResponseEntity.ok("advertisement is successfully added");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ERROR: advert is NOT added \n" + e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity getAdvertisement(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(advertisementService.getOneAdvertisement(id));
        } catch (AdvertisementNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Произошла ошибка");
        }
    }

    @GetMapping("/")
    public ResponseEntity getAllAdvertisement() {
        try {
            return ResponseEntity.ok(advertisementService.getAllAdvertisements());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Произошла ошибка");
        }
    }

}
