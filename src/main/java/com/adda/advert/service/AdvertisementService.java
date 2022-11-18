package com.adda.advert.service;

import com.adda.advert.Advertisement;
import com.adda.advert.dto.AdvertDTO;
import com.adda.advert.dto.AdvertUpdateDTO;
import com.adda.advert.exception.AdvertNotFoundException;
import com.adda.advert.filter.AdvertPage;
import com.adda.advert.filter.AdvertSearchCriteria;
import com.adda.user.User;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface AdvertisementService {

    Advertisement create(AdvertDTO dto, User user, List<MultipartFile> photos) throws IOException;

    Advertisement create(UUID id, AdvertDTO dto, User user, List<MultipartFile> photos) throws IOException;

    void addPhoto(List<MultipartFile> photos, UUID id) throws IOException;

    Advertisement update(Advertisement user, AdvertUpdateDTO advertDTO);

    Advertisement getAdvertById(UUID id) throws AdvertNotFoundException;

    Advertisement getAdvertById(UUID id, User user) throws AdvertNotFoundException;

    String deleteAdvertById(UUID id) throws AdvertNotFoundException;

    List<Advertisement> getAllByUser(long userId) throws AdvertNotFoundException;

    Page<Advertisement> getAdverts(AdvertPage advertPage,
                                   AdvertSearchCriteria advertSearchCriteria);

    boolean existsByTitleAndUsername(String title, String username);

    List<MultipartFile> getMultipartFiles(MultipartFile file1, MultipartFile file2,
                                          MultipartFile file3, MultipartFile file4,
                                          MultipartFile file5, MultipartFile file6,
                                          MultipartFile file7, MultipartFile file8);

}