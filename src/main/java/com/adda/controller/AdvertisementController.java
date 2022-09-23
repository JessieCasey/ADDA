package com.adda.controller;

import com.adda.DTO.advertisements.AdvertResponseDTO;
import com.adda.DTO.advertisements.AdvertisementDTO;
import com.adda.DTO.FilterDTO;
import com.adda.domain.AdvertisementEntity;
import com.adda.domain.RoleEntity;
import com.adda.domain.UserEntity;
import com.adda.exception.AdvertisementNotFoundException;
import com.adda.model.AdvertPage;
import com.adda.model.AdvertSearchCriteria;
import com.adda.repository.AdvertisementRepository;
import com.adda.service.AdvertisementService;
import com.adda.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.adda.service.UserService.getBearerTokenHeader;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("api/advert")
@Slf4j
public class AdvertisementController {

    private final AdvertisementService advertisementService;

    private final AdvertisementRepository advertisementRepository;

    private final UserServiceImpl userService;

    @Autowired
    public AdvertisementController(AdvertisementService advertisementService, AdvertisementRepository advertisementRepository, UserServiceImpl userService) {
        this.advertisementService = advertisementService;
        this.advertisementRepository = advertisementRepository;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> addAdvertisement(@RequestPart(name = "advertisement") AdvertisementDTO advertisementDTO,
                                              @RequestParam(name = "file1", required = false) MultipartFile file1, @RequestParam(name = "file2", required = false) MultipartFile file2,
                                              @RequestParam(name = "file3", required = false) MultipartFile file3, @RequestParam(name = "file4", required = false) MultipartFile file4,
                                              @RequestParam(name = "file5", required = false) MultipartFile file5, @RequestParam(name = "file6", required = false) MultipartFile file6,
                                              @RequestParam(name = "file7", required = false) MultipartFile file7, @RequestParam(name = "file8", required = false) MultipartFile file8
    ) throws Exception {
        log.info("[Post] Request to method 'addAdvertisement'");
        UserEntity user = userService.encodeUserFromToken(getBearerTokenHeader());
        try {
            if (advertisementRepository.existsByTitleAndUsername(advertisementDTO.getTitle(), user.getUsername())) {
                log.warn("Warning in method 'addAdvertisement': " + "Advertisement is already existed in your profile");
                return ResponseEntity.badRequest().body("advertisement is already existed in your profile");
            }

            List<MultipartFile> photos = advertisementService.getMultipartFiles(file1, file2, file3, file4, file5, file6, file7, file8);
            advertisementService.create(advertisementDTO, user, photos);

            return ResponseEntity.ok("advertisement is successfully added");
        } catch (Exception e) {
            log.error("Error in method 'addAdvertisement': " + e.getMessage());
            return ResponseEntity.badRequest().body("advertisement is not added \n" + e);
        }
    }

    @GetMapping("/{advertisement_id}")
    public ResponseEntity<?> getAdvertisementById(@PathVariable(value = "advertisement_id") UUID advertisementId) {
        log.info("[Get] Request to method 'getAdvertisementById'");
        try {
            if (getBearerTokenHeader() == null) {
                return ResponseEntity.ok(new AdvertResponseDTO(advertisementService.getAdvertById(advertisementId, null)));
            } else {
                UserEntity user = userService.encodeUserFromToken(getBearerTokenHeader());
                return ResponseEntity.ok(new AdvertResponseDTO(advertisementService.getAdvertById(advertisementId, user)));
            }
        } catch (AdvertisementNotFoundException e) {
            log.error("Error in method 'getAdvertisementById': " + e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("Error in method 'getAdvertisementById': " + e.getMessage());
            return ResponseEntity.badRequest().body("Advertisement isn't available");
        }
    }

    @DeleteMapping("/{advertisement_id}")
    public ResponseEntity<?> deleteAdvertisementById(@PathVariable(value = "advertisement_id") UUID advertisementId) {
        log.info("[Delete] Request to method 'deleteAdvertisementById'");
        try {
            UserEntity user = userService.encodeUserFromToken(getBearerTokenHeader());
            if (user.getRoles().stream().anyMatch(o -> "ROLE_ADMIN".equals(o.getName()))) {
                return ResponseEntity.ok("Advert \"" + advertisementService.deleteAdvertById(advertisementId) + "\" was deleted");
            }
            return ResponseEntity.badRequest().body("You are not admin");
        } catch (AdvertisementNotFoundException e) {
            log.error("Error type 'AdvertisementNotFoundException' in method 'deleteAdvertisementById': " + e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("Error in method 'deleteAdvertisementById': " + e.getMessage());
            return ResponseEntity.badRequest().body("Advertisement wasn't deleted \n" + e);
        }
    }

    @GetMapping("/category/{category_id}")
    public ResponseEntity<?> getAdvertisementByCategory(@PathVariable(value = "category_id") long categoryId) {
        log.info("[Get] Request to method 'getAdvertisementByCategory'");
        try {
            return ResponseEntity.ok(advertisementService.getAdvertsByCategory(categoryId));
        } catch (Exception e) {
            log.error("Error in method 'getAdvertisementByCategory': " + e.getMessage());
            return ResponseEntity.badRequest().body("There's no advertisements in that category");
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<?> getAdvertByPriceInRangeAndCategory(@RequestBody FilterDTO filterDTO) {
        log.info("[Get] Request to method 'getAdvertisementByPriceInRangeAndCategory'");
        try {
            return ResponseEntity.ok(advertisementService.getAdvertsByFilters(filterDTO));
        } catch (Exception e) {
            log.error("Error in method 'getAdvertisementByPriceInRangeAndCategory': " + e.getMessage());
            return ResponseEntity.badRequest().body("There's no advertisement in the price range: ["
                    + filterDTO.getStartPrice() + "-" + filterDTO.getEndPrice() + "] and in the category: [" + filterDTO.getCategoryName() + "]");
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getAdvertisementsByUser(@PathVariable long userId) {
        log.info("[Get] Request to method 'getAdvertisementsByUser'");
        try {
            return ResponseEntity.ok(advertisementService.getAllByUser(userId).stream()
                    .map(AdvertResponseDTO::new)
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            log.error("Error in method 'getAdvertisementsByUser': " + e.getMessage());
            return ResponseEntity.badRequest().body("The user with id: [" + userId + "] " + "doesn't have any advertisements");
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllAdvertisement(AdvertPage advertPage, AdvertSearchCriteria advertSearchCriteria) {
        log.info("[Get] Request to method 'getAllAdvertisement'");
        try {
            return new ResponseEntity<>(advertisementService.getAdverts(advertPage, advertSearchCriteria), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error in method 'getAllAdvertisement': " + e.getMessage());
            return ResponseEntity.badRequest().body("Advertisements aren't available" + e);
        }
    }
}
