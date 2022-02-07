package com.adda.service;

import com.adda.domain.PhotoEntity;
import com.adda.domain.UserEntity;
import com.adda.domain.AdvertisementEntity;
import com.adda.exception.AdvertisementNotFoundException;
import com.adda.model.Advertisement;
import com.adda.repository.UserRepository;
import com.adda.repository.advertisementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AdvertisementService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private advertisementRepository advertisementRepository;

    public Advertisement addAdvert(AdvertisementEntity advertisement, Long id) {
        UserEntity user = userRepository.findById(id).get();
        advertisement.setUser(user);
        advertisement.setUsername(user);
        advertisement.setPhotoLinks(new PhotoEntity());
        return Advertisement.toModel(advertisementRepository.save(advertisement));
    }

    public Advertisement addPhoto(PhotoEntity photoEntity, Long id) {
        AdvertisementEntity advertisement = advertisementRepository.findById(id).get();
        advertisement.setPhotoLinks(photoEntity);
        //advertisement.setPhotos(photo);
        return Advertisement.toModel(advertisementRepository.save(advertisement));
    }

    public Advertisement getOneAdvertisement(Long id) throws AdvertisementNotFoundException {
        AdvertisementEntity advertisement = advertisementRepository.findById(id).get();
        if (advertisement == null) {
            throw new AdvertisementNotFoundException("Advert не найден");
        }
        return Advertisement.toModel(advertisement);
    }

    public Iterable<AdvertisementEntity> getAllAdvertisements() {
        Iterable<AdvertisementEntity> advertisements = advertisementRepository.findAll();

        return advertisements;
    }
}
