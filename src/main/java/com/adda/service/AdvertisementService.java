package com.adda.service;

import com.adda.DTO.advertisements.AdvertisementDTO;
import com.adda.domain.AdvertisementEntity;
import com.adda.domain.UserEntity;
import com.adda.exception.AdvertisementNotFoundException;
import com.adda.model.AdvertPage;
import com.adda.model.AdvertSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface AdvertisementService {

    AdvertisementEntity create(AdvertisementDTO dto, UserEntity user, List<MultipartFile> photos) throws IOException;

    AdvertisementEntity create(UUID id, AdvertisementDTO dto, UserEntity user, List<MultipartFile> photos) throws IOException;

    void addPhoto(List<MultipartFile> photos, UUID id) throws IOException;

    AdvertisementEntity getAdvertById(UUID id, UserEntity user) throws AdvertisementNotFoundException;

    String deleteAdvertById(UUID id) throws AdvertisementNotFoundException;

    List<AdvertisementEntity> getAllByUser(long userId) throws AdvertisementNotFoundException;

    Page<AdvertisementEntity> getAdverts(AdvertPage advertPage,
                                       AdvertSearchCriteria advertSearchCriteria);

    boolean existsByTitleAndUsername(String title, String username);

    List<MultipartFile> getMultipartFiles(MultipartFile file1, MultipartFile file2,
                                          MultipartFile file3, MultipartFile file4,
                                          MultipartFile file5, MultipartFile file6,
                                          MultipartFile file7, MultipartFile file8);

}