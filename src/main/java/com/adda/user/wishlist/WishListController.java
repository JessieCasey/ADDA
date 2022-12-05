package com.adda.user.wishlist;

import com.adda.advert.dto.AdvertResponseDTO;
import com.adda.advert.repository.AdvertRepository;
import com.adda.advice.MessageResponse;
import com.adda.user.User;
import com.adda.user.service.UserDetailsImpl;
import com.adda.user.service.UserService;
import com.adda.user.wishlist.service.WishListService;
import com.adda.user.wishlist.service.WishListServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.UUID;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("api/wishlist")
@Slf4j
public class WishListController {

    private final AdvertRepository advertRepository;
    private final UserService userService;
    private final WishListService wishListService;

    @Autowired
    public WishListController(AdvertRepository advertRepository, UserService userService, WishListServiceImpl wishListService) {
        this.advertRepository = advertRepository;
        this.userService = userService;
        this.wishListService = wishListService;
    }

    @GetMapping
    public ResponseEntity<?> getWishListOfUser(@AuthenticationPrincipal UserDetailsImpl userDetails, WebRequest request) {
        log.info("[Get] Request to method 'getWishListOfUser'");
        try {
            User user = userService.getOneUser(userDetails.getId());
            if (user.getWishList() != null) {
                return ResponseEntity.ok(wishListService.getWishList(user).getAdverts().stream()
                        .map(AdvertResponseDTO::new)
                        .collect(Collectors.toList()));
            } else
                return ResponseEntity.ok("There's no adverts in the wishlist");

        } catch (Exception e) {
            log.error("Error in method 'getWishListOfUser': " + e.getMessage());
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage(), request));
        }
    }

    @PostMapping
    public ResponseEntity<?> addByIdToList(@RequestParam UUID advertId,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails, WebRequest request) {
        log.info("[Post] Request to method 'addByIdToList'");
        try {
            User user = userService.getOneUser(userDetails.getId());
            return ResponseEntity.ok(wishListService.addAdvertToWishList(user, advertRepository.getById(advertId)));
        } catch (Exception e) {
            log.error("Error in method 'addByIdToList': " + e.getMessage());
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage(), request));
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteByIdFromList(@RequestParam UUID advertId,
                                                @AuthenticationPrincipal UserDetailsImpl userDetails, WebRequest request) {
        log.info("[Delete] Request to method 'deleteByIdFromList'");
        try {
            User user = userService.getOneUser(userDetails.getId());
            return ResponseEntity.ok(wishListService.deleteAdvertFromWishList(user, advertRepository.getById(advertId)));
        } catch (Exception e) {
            log.error("Error in method 'deleteByIdFromList': " + e.getMessage());
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage(), request));
        }
    }
    @GetMapping("/{wishListId}")
    @PreAuthorize("@wishListController.idComparator(#userDetails.id, #wishListId) or hasRole('ADMIN')")
    public ResponseEntity<?> getWishListById(@PathVariable UUID wishListId,
                                             @AuthenticationPrincipal UserDetailsImpl userDetails,
                                             WebRequest request) {
        log.info("[Get] Request to method 'getWishListById'");
        try {

            if (wishListService.existById(wishListId)) {
                return ResponseEntity.ok(wishListService.getWishListById(wishListId).getAdverts().stream()
                        .map(AdvertResponseDTO::new)
                        .collect(Collectors.toList()));
            } else
                return ResponseEntity.ok("There's no adverts in the wishlist");

        } catch (Exception e) {
            log.error("Error in method 'getWishListOfUser': " + e.getMessage());
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage(), request));
        }
    }

    public boolean idComparator(long userId, UUID wishListId) {
        return wishListService.getWishListById(wishListId).getUserId() == userId;
    }
}
