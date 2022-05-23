package com.adda.service;

import com.adda.DTO.AdvertisementDTO;
import com.adda.DTO.FilterDTO;
import com.adda.domain.AdvertisementEntity;
import com.adda.domain.PhotoEntity;
import com.adda.domain.UserEntity;
import com.adda.exception.AdvertisementNotFoundException;
import com.adda.model.Advertisement;
import com.adda.repository.AdvertisementRepository;
import com.adda.repository.CategoriesRepository;
import com.adda.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.StreamSupport;

@Service
public class AdvertisementService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoriesRepository categoriesRepository;

    @Autowired
    private AdvertisementRepository advertisementRepository;

    public AdvertisementEntity addAdvert(AdvertisementDTO advertisementDTO, UserEntity user) {
        return addAdvert(null, advertisementDTO, user);
    }

    public AdvertisementEntity addAdvert(UUID id, AdvertisementDTO advertisementDTO, UserEntity user) {
        UUID advertisementID = Optional.ofNullable(id).orElse(UUID.randomUUID());
        PhotoEntity photoEntity = new PhotoEntity();
        photoEntity.setPhotos(new String[8]);

        AdvertisementEntity advertisement = new AdvertisementEntity(
                advertisementID,
                advertisementDTO.getTitle(),
                advertisementDTO.getPrice(),
                advertisementDTO.getDescription(),
                user.getEmail(),
                user.getUsername(),
                photoEntity,
                categoriesRepository.findById(advertisementDTO.getCategoryId()).get(),
                user,
                getCurrentTime(),
                QRcodeService.getUrlOfAdvertisement(advertisementID)
        );
        Advertisement.toModel(advertisementRepository.save(advertisement));
        return advertisement;
    }

    public String getCurrentTime() {
        String pattern = "dd-MM-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(new Date());
    }

    public Advertisement addPhoto(PhotoEntity photoEntity, UUID id) {
        AdvertisementEntity advertisement = advertisementRepository.findById(id);
        advertisement.setPhotos(photoEntity);
        //advertisement.setPhotos(photo);
        return Advertisement.toModel(advertisementRepository.save(advertisement));
    }

    public AdvertisementEntity getOneAdvertisementById(UUID id) throws AdvertisementNotFoundException {
        AdvertisementEntity advertisement = advertisementRepository.findById(id);

        advertisement.setViewers(advertisement.getViewers() + 1);

        advertisementRepository.save(advertisement);
        if (advertisement == null) {
            throw new AdvertisementNotFoundException("Advert is not found");
        }
        return advertisement;
    }

    public String deleteOneAdvertisementById(UUID id) throws AdvertisementNotFoundException {
        AdvertisementEntity advertisement = getOneAdvertisementById(id);
        String title = advertisement.getTitle();
        advertisementRepository.deleteById(id);

        return title;
    }

    public Iterable<AdvertisementEntity> getAdvertisementsByCategory(Long categoryId) throws AdvertisementNotFoundException {
        Iterable<AdvertisementEntity> advertisements = advertisementRepository.findAllByCategory(categoriesRepository.findById(categoryId).get());
        if (advertisements == null || (!StreamSupport.stream(advertisements.spliterator(), false).findAny().isPresent())) {
            throw new AdvertisementNotFoundException("No adverts in that category");
        }
        return advertisements;
    }

    public Iterable<AdvertisementEntity> getAdvertisementsByFilters(FilterDTO filterDTO) throws AdvertisementNotFoundException {
        Iterable<AdvertisementEntity> advertisements = null;
        if (filterDTO.getCategoryName() == null || filterDTO.getCategoryName().equals("")) {
            advertisements = advertisementRepository.findAllByPriceBetween(filterDTO.getStartPrice(), filterDTO.getEndPrice());
        } else {
            advertisements = advertisementRepository.findAllByPriceBetweenAndCategory(filterDTO.getStartPrice(), filterDTO.getEndPrice(), categoriesRepository.findByCategoryName(filterDTO.getCategoryName()));
        }

        if (advertisements == null) {
            throw new AdvertisementNotFoundException("No adverts in that category");
        }
        return advertisements;
    }

    public Iterable<AdvertisementEntity> getAllByUser(long userId) throws AdvertisementNotFoundException {
        Iterable<AdvertisementEntity> advertisements = null;
        if (!userRepository.findById(userId).isPresent()) {
            throw new AdvertisementNotFoundException("The user doesn't have any advertisements");
        } else {
            advertisements = advertisementRepository.findAllByUser(userRepository.findById(userId).get());
            if (advertisements != null) {
                return advertisements;
            } else {
                throw new AdvertisementNotFoundException("The user doesn't have any advertisements");
            }
        }
    }

    public Iterable<AdvertisementEntity> getAllAdvertisements() {
        return advertisementRepository.findAll();
    }

    public List<MultipartFile> getMultipartFiles(MultipartFile file1, MultipartFile file2, MultipartFile file3, MultipartFile file4, MultipartFile file5, MultipartFile file6, MultipartFile file7, MultipartFile file8) {
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
        return fileList;
    }

}
