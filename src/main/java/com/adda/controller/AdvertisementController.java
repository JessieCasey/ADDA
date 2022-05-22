package com.adda.controller;

import com.adda.DTO.AdvertisementDTO;
import com.adda.DTO.FilterDTO;
import com.adda.domain.AdvertisementEntity;
import com.adda.domain.PhotoEntity;
import com.adda.domain.UserEntity;
import com.adda.exception.AdvertisementNotFoundException;
import com.adda.repository.AdvertisementRepository;
import com.adda.repository.UserRepository;
import com.adda.service.AdvertisementService;
import com.adda.service.CustomUserDetailsService;
import com.adda.service.PhotoService;
import com.adda.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.adda.service.UserService.getBearerTokenHeader;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("api/advert")
public class AdvertisementController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private AdvertisementService advertisementService;

    @Autowired
    private AdvertisementRepository advertisementRepository;
    @Autowired
    private UserService userService;

    @PostMapping(value = "/add")
    public ResponseEntity addAdvertisement(
            @RequestPart(name = "advertisement") AdvertisementDTO advertisementDTO,
            @RequestParam(name = "file1", required = false) MultipartFile file1,
            @RequestParam(name = "file2", required = false) MultipartFile file2,
            @RequestParam(name = "file3", required = false) MultipartFile file3,
            @RequestParam(name = "file4", required = false) MultipartFile file4,
            @RequestParam(name = "file5", required = false) MultipartFile file5,
            @RequestParam(name = "file6", required = false) MultipartFile file6,
            @RequestParam(name = "file7", required = false) MultipartFile file7,
            @RequestParam(name = "file8", required = false) MultipartFile file8
    ) throws Exception {
        UserEntity user = userService.encodeUserFromToken(getBearerTokenHeader());

        try {
            if (advertisementRepository.existsByTitleAndUsername(advertisementDTO.getTitle(), user.getUsername())) {
                return ResponseEntity.badRequest().body("advertisement is already existed in your profile");
            }

            AdvertisementEntity advertisement = advertisementService.addAdvert(advertisementDTO, user);

            List<MultipartFile> fileList = new ArrayList();

            if (file1 != null)
                fileList.add(file1);
            if (file2 != null)
                fileList.add(file2);
            if (file3 != null)
                fileList.add(file3);
            if (file4 != null)
                fileList.add(file4);
            if (file5 != null)
                fileList.add(file5);
            if (file6 != null)
                fileList.add(file6);
            if (file7 != null)
                fileList.add(file7);
            if (file8 != null)
                fileList.add(file8);

            PhotoEntity photoEntity = PhotoService.uploadPhotoToAdvertisement(fileList);
            advertisementService.addPhoto(photoEntity, advertisement.getId());

            return ResponseEntity.ok("advertisement is successfully added");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("advertisement is not added \n" + e);
        }
    }

    @GetMapping("/{advertisementId}")
    public ResponseEntity getAdvertisementByTitle(@PathVariable UUID advertisementId) {
        try {
            return ResponseEntity.ok(advertisementService.getOneAdvertisementById(advertisementId));
        } catch (AdvertisementNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Advertisement isn't available");
        }
    }

    @GetMapping("/category/{category_id}")
    public ResponseEntity getAdvertisementByCategory(@PathVariable long category_id) {
        try {
            return ResponseEntity.ok(advertisementService.getAdvertisementsByCategory(category_id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("There's no advertisements in that category");
        }
    }

    @GetMapping("/filter")
    public ResponseEntity getAdvertisementByPriceInRangeAndCategory(@RequestBody FilterDTO filterDTO) {
        try {
            return ResponseEntity.ok(advertisementService.getAdvertisementsByFilters(filterDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("There's no advertisement in the price range: ["
                    + filterDTO.getStartPrice() + "-" + filterDTO.getEndPrice() + "] and in the category: [" + filterDTO.getCategoryName() + "]");
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity getAdvertisementsByUser(@PathVariable long userId) {
        try {
            return ResponseEntity.ok(advertisementService.getAllByUser(userId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("The user with id: [" + userId + "] " + "doesn't have any advertisements");
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
