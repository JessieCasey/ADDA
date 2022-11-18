package com.adda.advert;

import com.adda.advert.dto.AdvertResponseDTO;
import com.adda.advert.dto.AdvertisementDTO;
import com.adda.advert.dto.AdvertisementUpdateDTO;
import com.adda.advert.exception.AdvertisementNotFoundException;
import com.adda.advert.filter.AdvertPage;
import com.adda.advert.filter.AdvertSearchCriteria;
import com.adda.user.User;
import com.adda.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.adda.user.service.UserService.getBearerTokenHeader;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("api/advert")
@Slf4j
public class AdvertisementController {

    private final AdvertisementService advertisementService;
    private final UserService userService;

    @Autowired
    public AdvertisementController(AdvertisementService advertisementService, UserService userService) {
        this.advertisementService = advertisementService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> addAdvertisement(
            @Valid @RequestPart(name = "advertisement") AdvertisementDTO advertisementDTO,
            @RequestParam(name = "file1", required = false) MultipartFile file1, @RequestParam(name = "file2", required = false) MultipartFile file2,
            @RequestParam(name = "file3", required = false) MultipartFile file3, @RequestParam(name = "file4", required = false) MultipartFile file4,
            @RequestParam(name = "file5", required = false) MultipartFile file5, @RequestParam(name = "file6", required = false) MultipartFile file6,
            @RequestParam(name = "file7", required = false) MultipartFile file7, @RequestParam(name = "file8", required = false) MultipartFile file8
    ) {
        log.info("[Post] Request to method 'addAdvertisement'");
        User user = userService.encodeUserFromToken(getBearerTokenHeader());
        try {
            if (advertisementService.existsByTitleAndUsername(advertisementDTO.getTitle(), user.getUsername())) {
                log.warn("Warning in method 'addAdvertisement': " + "Advertisement is already existed in your profile");
                return ResponseEntity.badRequest().body("Advertisement is already existed in your profile");
            }

            List<MultipartFile> photos = advertisementService.getMultipartFiles(file1, file2, file3, file4, file5, file6, file7, file8);
            advertisementService.create(advertisementDTO, user, photos);

            return ResponseEntity.ok("Advertisement is successfully added");
        } catch (Exception e) {
            log.error("Error in method 'addAdvertisement': " + e.getMessage());
            return ResponseEntity.badRequest().body("advertisement is not added \n" + e);
        }

    }

    @PutMapping("/{advertId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> updateAdvertisement(@PathVariable UUID advertId, @Valid @RequestBody AdvertisementUpdateDTO advertDTO) {
        log.info("[PUT] Request to 'updateAdvertisement'");

        User user = userService.encodeUserFromToken(getBearerTokenHeader());

        AdvertisementEntity advertById = advertisementService.getAdvertById(advertId);
        log.info("[PUT] Request to 'updateAdvertisement': Advert is found in DB");

        if (!(user.getRoles().stream().anyMatch(o -> "ROLE_ADMIN".equals(o.getName())) || advertById.getUser().equals(user))) {
            log.warn("[PUT] Request to 'updateAdvertisement': User is not an admin and is not the owner of the advert");
            return ResponseEntity.badRequest().body("You are not admin and not the owner of advert");
        }

        AdvertisementEntity update = advertisementService.update(advertById, advertDTO);

        log.info("[PUT] Request to 'updateAdvertisement': Advert is updated");
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(update.getId())
                .toUri();

        return ResponseEntity.created(location).body(new AdvertResponseDTO(update));
    }

    @GetMapping("/{advertisementId}")
    public ResponseEntity<?> getAdvertisementById(@PathVariable UUID advertisementId) {
        log.info("[Get] Request to method 'getAdvertisementById'");
        try {
            if (getBearerTokenHeader() == null) {
                return ResponseEntity.ok(new AdvertResponseDTO(advertisementService.getAdvertById(advertisementId)));
            } else {
                User user = userService.encodeUserFromToken(getBearerTokenHeader());
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

    @DeleteMapping("/{advertisementId}")
    public ResponseEntity<?> deleteAdvertisementById(@PathVariable UUID advertisementId) {
        log.info("[Delete] Request to method 'deleteAdvertisementById'");
        try {
            User user = userService.encodeUserFromToken(getBearerTokenHeader());
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
            Page<AdvertResponseDTO> dtoPage = advertisementService.getAdverts(advertPage, advertSearchCriteria).map(AdvertResponseDTO::new);
            return new ResponseEntity<>(dtoPage, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error in method 'getAllAdvertisement': " + e.getMessage());
            return ResponseEntity.badRequest().body("Advertisements aren't available" + e);
        }
    }
}
