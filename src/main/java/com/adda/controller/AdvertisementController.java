package com.adda.controller;

import com.adda.DTO.AdvertisementDTO;
import com.adda.DTO.FilterDTO;
import com.adda.domain.UserEntity;
import com.adda.exception.AdvertisementNotFoundException;
import com.adda.repository.UserRepository;
import com.adda.service.AdvertisementService;
import com.adda.service.CustomUserDetailsService;
import com.adda.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    // eyJhbGciOiJIUzI1NiJ9.eyJuYW1lIjoiSm9lIENvZGVyIn0.5dlp7GmziL2QS06sZgK4mtaqv0_xX4oFUuTDh1zHK4U



    //
    @PostMapping("/add")
    public HttpEntity<String> addAdvertisement(@RequestBody AdvertisementDTO advertisementDTO) {
        try {
            ResponseEntity<String> tokenEncoded = UserService.encodeToken(advertisementDTO.getToken());
            System.out.println(tokenEncoded.getBody());

            UserEntity user = new UserEntity();
            //advertisementService.addAdvert(advertisementDTO, user);
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
