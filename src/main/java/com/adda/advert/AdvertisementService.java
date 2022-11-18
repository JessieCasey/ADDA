package com.adda.advert;

import com.adda.advert.dto.AdvertisementDTO;
import com.adda.advert.dto.AdvertisementUpdateDTO;
import com.adda.advert.exception.AdvertisementNotFoundException;
import com.adda.advert.filter.AdvertPage;
import com.adda.advert.filter.AdvertSearchCriteria;
import com.adda.user.User;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface AdvertisementService {

    AdvertisementEntity create(AdvertisementDTO dto, User user, List<MultipartFile> photos) throws IOException;

    AdvertisementEntity create(UUID id, AdvertisementDTO dto, User user, List<MultipartFile> photos) throws IOException;

    void addPhoto(List<MultipartFile> photos, UUID id) throws IOException;

    AdvertisementEntity update(AdvertisementEntity user, AdvertisementUpdateDTO advertDTO);

    AdvertisementEntity getAdvertById(UUID id) throws AdvertisementNotFoundException;

    AdvertisementEntity getAdvertById(UUID id, User user) throws AdvertisementNotFoundException;

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