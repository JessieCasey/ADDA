package com.adda.controller;

import com.adda.domain.AdvertisementEntity;
import com.adda.exception.AdvertisementNotFoundException;
import com.adda.service.AdvertisementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/upload")
@Slf4j
public class PhotoController {

    private final AdvertisementService advertisementService;

    @Lazy
    @Autowired
    public PhotoController(AdvertisementService advertisementService) {
        this.advertisementService = advertisementService;
    }

    @PostMapping("/{advertId}")
    public ResponseEntity<?> uploadPhotoToAdvert(
            @PathVariable UUID advertId,
            @RequestParam(name = "file1", required = false) MultipartFile file1, @RequestParam(name = "file2", required = false) MultipartFile file2,
            @RequestParam(name = "file3", required = false) MultipartFile file3, @RequestParam(name = "file4", required = false) MultipartFile file4,
            @RequestParam(name = "file5", required = false) MultipartFile file5, @RequestParam(name = "file6", required = false) MultipartFile file6,
            @RequestParam(name = "file7", required = false) MultipartFile file7, @RequestParam(name = "file8", required = false) MultipartFile file8
    ) throws IOException, AdvertisementNotFoundException {

        log.info("[Post] Request to method 'uploadPhotoToAdvert'");

        AdvertisementEntity advert = advertisementService.getAdvertById(advertId);
        if (advert == null) {
            log.warn("Warning in method 'uploadPhotoToAdvert': advert is null");
            return new ResponseEntity<>("Files are NOT uploaded successfully" + "advert is null", HttpStatus.BAD_REQUEST);
        }

        List<MultipartFile> photos = advertisementService.getMultipartFiles(file1, file2, file3, file4, file5, file6, file7, file8);
        advertisementService.addPhoto(photos, advert.getId());

        return new ResponseEntity<>("Files are uploaded successfully", HttpStatus.OK);
    }
}
