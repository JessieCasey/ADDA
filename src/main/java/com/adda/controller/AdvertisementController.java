package com.adda.controller;

import com.adda.domain.AdvertisementEntity;
import com.adda.exception.AdvertisementNotFoundException;
import com.adda.service.AdvertisementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/adv")
public class AdvertisementController {
    @Autowired
    private AdvertisementService advertisementService;

    @PostMapping("/")
    public HttpEntity<String> addAdvertisement(@RequestBody AdvertisementEntity advertisement, @RequestParam Long userId) {
        try {
            advertisementService.addAdvert(advertisement, userId);
            return ResponseEntity.ok("advertisement is successfully added");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ERROR: advert is NOT added");
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
