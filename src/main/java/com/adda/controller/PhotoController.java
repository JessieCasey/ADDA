package com.adda.controller;

import com.adda.domain.AdvertisementEntity;
import com.adda.domain.PhotoEntity;
import com.adda.domain.UserEntity;
import com.adda.repository.AdvertisementRepository;
import com.adda.repository.UserRepository;
import com.adda.service.AdvertisementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/upl")
public class PhotoController {

    @Value("/Users/artem/Documents/JavaProjects/adda/uploads/")
    private String uploadPath;

    @Autowired
    private AdvertisementService advertisementService;

    @Autowired
    private AdvertisementRepository advertisementRepository;
    @Autowired
    private UserRepository userRepository;


    @PostMapping
    public ResponseEntity<Object> uploadPhotoToAdvertisement(Principal authUser,
                                                             @RequestParam UUID advertId,
                                                             @RequestParam(name = "file1", required = false) MultipartFile file1,
                                                             @RequestParam(name = "file2", required = false) MultipartFile file2,
                                                             @RequestParam(name = "file3", required = false) MultipartFile file3,
                                                             @RequestParam(name = "file4", required = false) MultipartFile file4,
                                                             @RequestParam(name = "file5", required = false) MultipartFile file5,
                                                             @RequestParam(name = "file6", required = false) MultipartFile file6,
                                                             @RequestParam(name = "file7", required = false) MultipartFile file7,
                                                             @RequestParam(name = "file8", required = false) MultipartFile file8) throws IOException {
        UserEntity user = userRepository.findByUsernameOrEmail(authUser.getName(), authUser.getName())
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username or email:" + authUser.getName()));

        // check if the current user has the advertisement
        AdvertisementEntity advertisement = advertisementRepository.findById(advertId);
        if (advertisement.getUser().equals(user)) {
            String[] arrayOfPath = new String[8];
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

            String path = null;
            File convertFile;
            for (int i = 0; i < fileList.size(); i++) {
                path = uploadPath + UUID.randomUUID() + fileList.get(i).getOriginalFilename();
                arrayOfPath[i] = path;

                convertFile = new File(path);
                convertFile.createNewFile();

                FileOutputStream fileOutputStream = new FileOutputStream(convertFile);
                fileOutputStream.write(fileList.get(i).getBytes());
                fileOutputStream.close();
            }

            PhotoEntity photoEntity = new PhotoEntity();
            photoEntity.setPhotos(arrayOfPath);

            advertisementService.addPhoto(photoEntity, advertId);
            return new ResponseEntity<>("Files are uploaded successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Files are not uploaded", HttpStatus.BAD_REQUEST);
        }
    }
}
