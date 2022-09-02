package com.adda.service;

import com.adda.DTO.AdvertisementDTO;
import com.adda.DTO.FilterDTO;
import com.adda.domain.AdvertisementEntity;
import com.adda.domain.PhotoEntity;
import com.adda.domain.UserEntity;
import com.adda.exception.AdvertisementNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface AdvertisementService {
    AdvertisementEntity create(AdvertisementDTO advertisementDTO, UserEntity user);

    AdvertisementEntity create(UUID id, AdvertisementDTO advertisementDTO, UserEntity user);

    void addPhoto(PhotoEntity photoEntity, UUID id);

    AdvertisementEntity getOneAdvertisementById(UUID id, UserEntity user) throws AdvertisementNotFoundException;

    String deleteOneAdvertisementById(UUID id) throws AdvertisementNotFoundException;

    Iterable<AdvertisementEntity> getAdvertisementsByCategory(Long categoryId) throws AdvertisementNotFoundException;

    Iterable<AdvertisementEntity> getAdvertisementsByFilters(FilterDTO filterDTO) throws AdvertisementNotFoundException;

    Iterable<AdvertisementEntity> getAllByUser(long userId) throws AdvertisementNotFoundException;

    Iterable<AdvertisementEntity> getAllAdvertisements();

    List<MultipartFile> getMultipartFiles(MultipartFile file1, MultipartFile file2, MultipartFile file3, MultipartFile file4, MultipartFile file5, MultipartFile file6, MultipartFile file7, MultipartFile file8);

}