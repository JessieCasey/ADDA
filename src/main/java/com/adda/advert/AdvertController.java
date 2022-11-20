package com.adda.advert;

import com.adda.advert.dto.AdvertDTO;
import com.adda.advert.dto.AdvertResponseDTO;
import com.adda.advert.dto.AdvertUpdateDTO;
import com.adda.advert.repository.AdvertModel;
import com.adda.advert.repository.AdvertModelAssembler;
import com.adda.advert.service.AdvertService;
import com.adda.advice.MessageException;
import com.adda.user.service.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("api/advert")
@Slf4j
public class AdvertController {

    private final AdvertService advertService;
    private final PagedResourcesAssembler<Advert> assembler;
    private final AdvertModelAssembler modelAssembler;

    @Autowired
    public AdvertController(AdvertService advertService,
                            PagedResourcesAssembler<Advert> assembler, AdvertModelAssembler modelAssembler) {
        this.advertService = advertService;
        this.assembler = assembler;
        this.modelAssembler = modelAssembler;
    }

    /**
     * @param advertDTO       DTO for creating advertisement
     * @param file1 (n)       file(N) Multipart files are such as photos for advertisement
     * @return ResponseEntity<AdvertResponseDTO> object in case of success.
     */
    @PostMapping
    public ResponseEntity<?> addAdvert(
            @Valid @RequestPart(name = "advert") AdvertDTO advertDTO,
            @RequestParam(name = "file1", required = false) MultipartFile file1, @RequestParam(name = "file2", required = false) MultipartFile file2,
            @RequestParam(name = "file3", required = false) MultipartFile file3, @RequestParam(name = "file4", required = false) MultipartFile file4,
            @RequestParam(name = "file5", required = false) MultipartFile file5, @RequestParam(name = "file6", required = false) MultipartFile file6,
            @RequestParam(name = "file7", required = false) MultipartFile file7, @RequestParam(name = "file8", required = false) MultipartFile file8,
            @AuthenticationPrincipal UserDetailsImpl user, WebRequest request
    ) {
        log.info("[Post] Request to method 'addAdvert'");
        try {
            if (advertService.existsByTitleAndUsername(advertDTO.getTitle(), user.getUsername())) {
                log.warn("Warning in method 'addAdvert': " + "Advertisement is already existed in your profile");
                return ResponseEntity.badRequest().body("Advert with a title '" + advertDTO.getTitle() + "' is already existed in your profile");
            }

            List<MultipartFile> photos = advertService.getMultipartFiles(file1, file2, file3, file4, file5, file6, file7, file8);

            return ResponseEntity.ok(new AdvertResponseDTO(advertService.create(advertDTO, user, photos)));
        } catch (Exception e) {
            log.error("Error in method 'addAdvert': " + e.getMessage());
            return ResponseEntity.badRequest().body(new MessageException(e.getMessage(), request));
        }
    }

    /**
     * @param title       Filter for the first Name if required
     * @param description Filter for the last Name if required
     * @param page        number of the page returned
     * @param size        number of entries in each page
     * @param sortList    list of columns to sort on
     * @param sortOrder   sort order. Can be ASC or DESC
     * @return PagedModel object in Hateoas with adverts after filtering and sorting
     */
    @GetMapping("/v4/page")
    public PagedModel<AdvertModel> fetchAdvertsWithPagination(
            @RequestParam(defaultValue = "") String title,
            @RequestParam(defaultValue = "") String description,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size,
            @RequestParam(defaultValue = "") List<String> sortList,
            @RequestParam(defaultValue = "DESC") Sort.Direction sortOrder) {
        log.info("[GET] Request to method 'fetchAdvertsWithPagination'");
        Page<Advert> advertPage = advertService.fetchAdvertsWithFilteringAndSorting(title, description, page, size, sortList, sortOrder.toString());
        return assembler.toModel(advertPage, modelAssembler);
    }

    /**
     * @param title Filter for the first Name if required
     * @param description  Filter for the last Name if required
     * @param page      number of the page returned
     * @param size      number of entries in each page
     * @param sortList  list of columns to sort on
     * @param sortOrder sort order. Can be ASC or DESC
     * @return Page object with customers after filtering and sorting
     */
    @GetMapping("/v3/page")
    public Page<Advert> fetchUsersWithPageInterfaceAndSorted(
            @RequestParam(defaultValue = "") String title,
            @RequestParam(defaultValue = "") String description,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size,
            @RequestParam(defaultValue = "") List<String> sortList,
            @RequestParam(defaultValue = "DESC") Sort.Direction sortOrder) {
        log.info("[GET] Request to method 'fetchUsersWithPageInterfaceAndSorted'");
        return advertService.fetchAdvertsWithFilteringAndSorting(title, description, page, size, sortList, sortOrder.toString());
    }

    /**
     * @param title   Filter for the title if required
     * @param description  Filter for the description if required
     * @param page    number of the page returned
     * @param size    number of entries in each page
     * @return Page object with adverts after filtering
     */
    @GetMapping("/v2/page")
    public Page<Advert> fetchUsersWithPageInterface(
            @RequestParam(defaultValue = "") String title,
            @RequestParam(defaultValue = "") String description,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {
        return advertService.fetchAdvertDataAsPageWithFiltering(title, description, page, size);
    }

    /**
     * @param title Filter for the title if required
     * @param description  Filter for the description if required
     * @return List of filtered adverts
     */
    @GetMapping("/v1/page")
    public List<Advert> fetchUsersAsFilteredList(@RequestParam(defaultValue = "") String title,
                                                 @RequestParam(defaultValue = "") String description) {
        return advertService.fetchFilteredAdvertsAsList(title, description);
    }

    /**
     * @return List of all adverts
     */
    @GetMapping("/v0/page")
    public List<Advert> fetchAdvertsAsList() {
        return advertService.fetchAdvertDataAsList();
    }


    @PutMapping("/{advertId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("@advertController.idComparator(#user.id, #advertId) or hasRole('ADMIN')")
    public ResponseEntity<?> updateAdvert(@PathVariable UUID advertId,
                                          @Valid @RequestBody AdvertUpdateDTO advertDTO,
                                          @AuthenticationPrincipal UserDetailsImpl user,
                                          WebRequest request) {
        log.info("[PUT] Request to 'updateAdvert'");
        try {
            advertDTO.setId(advertId);
            Advert update = advertService.update(advertDTO);

            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(update.getId())
                    .toUri();

            log.info("[PUT] Request to 'updateAdvert': Advert is updated");
            return ResponseEntity.created(location).body(new AdvertResponseDTO(update));
        } catch (Exception e) {
            log.error("Error in method 'updateAdvert': " + e.getMessage());
            return ResponseEntity.badRequest().body(new MessageException(e.getMessage(), request));
        }
    }

    @DeleteMapping("/{advertId}")
    @PreAuthorize("@advertController.idComparator(#user.id, #advertId) or hasRole('ADMIN')")
    public ResponseEntity<?> deleteAdvertById(@PathVariable UUID advertId,
                                              @AuthenticationPrincipal UserDetailsImpl user,
                                              WebRequest request) {
        log.info("[DELETE] Request to method 'deleteAdvertById'");
        try {
            return ResponseEntity.ok("Advert \"" + advertService.deleteAdvertById(advertId) + "\" was deleted");
        } catch (Exception e) {
            log.error("Error in method 'deleteAdvertById': " + e.getMessage());
            return ResponseEntity.badRequest().body(new MessageException(e.getMessage(), request));
        }
    }

    @GetMapping("/{advertId}")
    public ResponseEntity<?> getAdvertById(@PathVariable UUID advertId,
                                           @AuthenticationPrincipal UserDetailsImpl user,
                                           WebRequest request) {
        log.info("[Get] Request to method 'getAdvertById'");
        try {
            if (user == null) {
                return ResponseEntity.ok(new AdvertResponseDTO(advertService.getAdvertById(advertId)));
            } else {
                return ResponseEntity.ok(new AdvertResponseDTO(advertService.getAdvertById(advertId, user)));
            }
        } catch (Exception e) {
            log.error("Error in method 'getAdvertById': " + e.getMessage());
            return ResponseEntity.badRequest().body(new MessageException(e.getMessage(), request));
        }
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getAdvertsByUser(@PathVariable long userId) {
        log.info("[Get] Request to method 'getAdvertisementsByUser'");
        try {
            return ResponseEntity.ok(advertService.getAllByUser(userId).stream()
                    .map(AdvertResponseDTO::new)
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            log.error("Error in method 'getAdvertisementsByUser': " + e.getMessage());
            return ResponseEntity.badRequest().body("The user with id: [" + userId + "] " + "doesn't have any advertisements");
        }
    }

    public boolean idComparator(long userId, UUID advertId) {
        return advertService.getAdvertById(advertId).getUser().getId() == userId;
    }
}
