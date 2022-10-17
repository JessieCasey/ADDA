package com.adda.controller;

import com.adda.DTO.advertisements.AdvertResponseDTO;
import com.adda.domain.UserEntity;
import com.adda.repository.AdvertisementRepository;
import com.adda.service.UserService;
import com.adda.service.WishListService;
import com.adda.service.impl.WishListServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.stream.Collectors;

import static com.adda.service.UserService.getBearerTokenHeader;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("api/advert/wishlist")
@Slf4j
public class WishListController {

    private final AdvertisementRepository advertisementRepository;
    private final UserService userService;
    private final WishListService wishListService;

    @Lazy
    @Autowired
    public WishListController(AdvertisementRepository advertisementRepository, UserService userService, WishListServiceImpl wishListService) {
        this.advertisementRepository = advertisementRepository;
        this.userService = userService;
        this.wishListService = wishListService;
    }

    @GetMapping
    public ResponseEntity<?> getWishListOfUser() {
        log.info("[Get] Request to method 'getWishListOfUser'");
        try {
            UserEntity user = userService.encodeUserFromToken(getBearerTokenHeader());
            if (user.getWishList() != null) {
                return ResponseEntity.ok(wishListService.getWishList(user).getAdvertisements().stream()
                        .map(AdvertResponseDTO::new)
                        .collect(Collectors.toList()));

            } else
                return ResponseEntity.ok("No adverts in list");

        } catch (Exception e) {
            log.error("Error in method 'getWishListOfUser': " + e.getMessage());
            return ResponseEntity.badRequest().body("Wish list isn't available \n" + e);
        }
    }

    @PostMapping("/{advertisementId}")
    public ResponseEntity<?> addById(@PathVariable UUID advertisementId) {
        log.info("[Post] Request to method 'addById'");
        try {
            UserEntity user = userService.encodeUserFromToken(getBearerTokenHeader());
            return ResponseEntity.ok(wishListService.addAdvertToWishList(user, advertisementRepository.getById(advertisementId)));
        } catch (Exception e) {
            log.error("Error in method 'addById': " + e.getMessage());
            return ResponseEntity.badRequest().body("Wish list isn't available \n" + e);
        }
    }

    @DeleteMapping("/{advertisementId}")
    public ResponseEntity<?> deleteById(@PathVariable UUID advertisementId) {
        log.info("[Delete] Request to method 'deleteById'");
        try {
            UserEntity user = userService.encodeUserFromToken(getBearerTokenHeader());
            return ResponseEntity.ok(wishListService.deleteAdvertFromWishList(user, advertisementRepository.getById(advertisementId)));
        } catch (Exception e) {
            log.error("Error in method 'deleteById': " + e.getMessage());
            return ResponseEntity.badRequest().body("Wish list isn't available \n" + e);
        }
    }

}
