package com.adda.controller;

import com.adda.domain.AdvertisementEntity;
import com.adda.domain.PhotoEntity;
import com.adda.repository.AdvertisementRepository;
import com.adda.service.AdvertisementService;
import com.adda.service.PhotoService;
import com.adda.service.photoService.responses.OptionalResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/upl")
public class PhotoController {

    @Autowired
    private AdvertisementService advertisementService;

    @Autowired
    private AdvertisementRepository advertisementRepository;

    @PostMapping
    public ResponseEntity<Object> uploadPhotoToAdvertisement(
            @RequestParam UUID advertId,
            @RequestParam(name = "file1", required = false) MultipartFile file1,
            @RequestParam(name = "file2", required = false) MultipartFile file2,
            @RequestParam(name = "file3", required = false) MultipartFile file3,
            @RequestParam(name = "file4", required = false) MultipartFile file4,
            @RequestParam(name = "file5", required = false) MultipartFile file5,
            @RequestParam(name = "file6", required = false) MultipartFile file6,
            @RequestParam(name = "file7", required = false) MultipartFile file7,
            @RequestParam(name = "file8", required = false) MultipartFile file8) throws IOException {

        // check if the current user has the advertisement
        AdvertisementEntity advertisement = advertisementRepository.findById(advertId);
        if (advertisement == null) {
            return new ResponseEntity<>("Files are NOT uploaded successfully" + "advertisement is null", HttpStatus.BAD_REQUEST);
        }

        String[] arrayOfPath = new String[8];
        String[] fileNames = new String[8];
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

        String uniqueFileName = null;
        String[] imagesBase64 = new String[8];
        for (int i = 0; i < fileList.size(); i++) {
            uniqueFileName = UUID.randomUUID() + fileList.get(i).getOriginalFilename();
            fileNames[i] = uniqueFileName;

            imagesBase64[i] = Base64.getEncoder().encodeToString(fileList.get(i).getBytes());
        }

        PhotoEntity photoEntity = new PhotoEntity();
        OptionalResponse[] optionalResponse = PhotoService.uploadPhotoToServer(imagesBase64, fileNames);
        for (int i = 0; i < optionalResponse.length; i++) {
            if (optionalResponse[i] != null) {
                arrayOfPath[i] = optionalResponse[i].get().getResponseData().getImageUrl();
            }
        }

        photoEntity.setPhotos(arrayOfPath);
        advertisementService.addPhoto(photoEntity, advertId);
        return new ResponseEntity<>("Files are uploaded successfully", HttpStatus.OK);
    }
}
