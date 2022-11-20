package com.adda.advert.photo;

import com.adda.advert.Advert;
import com.adda.advert.dto.AdvertResponseDTO;
import com.adda.advert.service.AdvertService;
import com.adda.advert.exception.AdvertNotFoundException;
import com.adda.advice.MessageResponse;
import com.adda.user.service.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/upload")
@Slf4j
public class PhotoController {

    private final AdvertService advertService;

    /**
     * Constructor for {@link PhotoController}.
     *
     * @param advertService {@link AdvertService}
     */
    @Autowired
    public PhotoController(AdvertService advertService) {
        this.advertService = advertService;
    }

    /**
     * [Post] Method that adding adverts to the user.  {@link Advert}
     *
     * @param advertId The id of the advertisement. {@link UUID}
     * @param file1    file(max 8) Multipart files for advertisement. {@link MultipartFile}
     * @param user     Authenticated user. {@link UserDetailsImpl}
     * @return ResponseEntity<AdvertResponseDTO> object in case of success. {@link ResponseEntity< AdvertResponseDTO >}
     * @author Artem Komarov
     */
    @PostMapping("/{advertId}")
    @PreAuthorize("@advertController.idComparator(#user.id, #advertId) or hasRole('ADMIN')")
    public ResponseEntity<?> uploadPhotoToAdvert(
            @PathVariable UUID advertId,
            @RequestParam(name = "file1", required = false) MultipartFile file1, @RequestParam(name = "file2", required = false) MultipartFile file2,
            @RequestParam(name = "file3", required = false) MultipartFile file3, @RequestParam(name = "file4", required = false) MultipartFile file4,
            @RequestParam(name = "file5", required = false) MultipartFile file5, @RequestParam(name = "file6", required = false) MultipartFile file6,
            @RequestParam(name = "file7", required = false) MultipartFile file7, @RequestParam(name = "file8", required = false) MultipartFile file8,
            @AuthenticationPrincipal UserDetailsImpl user, WebRequest request
    ) throws AdvertNotFoundException {
        try {
            log.info("[Post] Request to method 'uploadPhotoToAdvert'");

            Advert advert = advertService.getAdvertById(advertId);
            if (advert == null) {
                log.warn("Warning in method 'uploadPhotoToAdvert': advert is null");
                return new ResponseEntity<>("Files are NOT uploaded successfully" + "advert is null", HttpStatus.BAD_REQUEST);
            }

            List<MultipartFile> photos = advertService.getMultipartFiles(file1, file2, file3, file4, file5, file6, file7, file8);
            advertService.addPhoto(photos, advert.getId());

            return new ResponseEntity<>("Files are uploaded successfully", HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error in method 'uploadPhotoToAdvert': " + e.getMessage());
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage(), request));
        }

    }
}
