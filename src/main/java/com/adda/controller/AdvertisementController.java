package com.adda.controller;

import com.adda.DTO.AdvertisementDTO;
import com.adda.DTO.FilterDTO;
import com.adda.domain.UserEntity;
import com.adda.exception.AdvertisementNotFoundException;
import com.adda.repository.UserRepository;
import com.adda.service.AdvertisementService;
import com.adda.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("api/advert")
public class AdvertisementController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private AdvertisementService advertisementService;

    @PostMapping("/add")
    public HttpEntity<String> addAdvertisement(@RequestBody AdvertisementDTO advertisementDTO, Principal authUser) {
        try {
            UserEntity user = userRepository.findByUsernameOrEmail(authUser.getName(), authUser.getName())
                    .orElseThrow(() ->
                            new UsernameNotFoundException("User not found with username or email:" + authUser.getName()));

            advertisementService.addAdvert(advertisementDTO, user);
            return ResponseEntity.ok("advertisement is successfully added");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ERROR: advert is NOT added \n" + e);
        }
    }

    @GetMapping("/{advertisementTitle}")
    public ResponseEntity getAdvertisementByTitle(@PathVariable String advertisementTitle) {
        try {
            return ResponseEntity.ok(advertisementService.getOneAdvertisementByTitle(advertisementTitle));
        } catch (AdvertisementNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Advertisement isn't available");
        }
    }

    @GetMapping("/category/{category_id}")
    public ResponseEntity getAdvertisementByCategory(@PathVariable String category_id) {
        try {
            return ResponseEntity.ok(advertisementService.getAdvertisementsByCategory(category_id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Advertisement isn't available");
        }
    }

    @GetMapping("/filter")
    public ResponseEntity getAdvertisementByPriceInRangeAndCategory(@RequestBody FilterDTO filterDTO) {
        try {
            return ResponseEntity.ok(advertisementService.getAdvertisementsByFilters(filterDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Advertisement isn't available");
        }
    }

    @GetMapping("/")
    public ResponseEntity getAllAdvertisement() {
        try {
            return ResponseEntity.ok(advertisementService.getAllAdvertisements());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Advertisements aren't available" + e);
        }
    }
}
