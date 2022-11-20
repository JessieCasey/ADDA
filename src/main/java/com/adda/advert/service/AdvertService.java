package com.adda.advert.service;

import com.adda.advert.Advert;
import com.adda.advert.dto.AdvertDTO;
import com.adda.advert.dto.AdvertUpdateDTO;
import com.adda.advert.exception.AdvertNotFoundException;
import com.adda.user.User;
import com.adda.user.service.UserDetailsImpl;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface AdvertService {
    Page<Advert> fetchAdvertsWithFilteringAndSorting(String title, String description, int page, int size, List<String> sortList, String sortOrder);

    List<Advert> fetchAdvertDataAsList();

    List<Advert> fetchFilteredAdvertsAsList(String title, String description);

    Page<Advert> fetchAdvertDataAsPageWithFiltering(String title, String description, int page, int size);

    Advert create(AdvertDTO dto, UserDetailsImpl userDetails, List<MultipartFile> photos) throws IOException;

    Advert create(UUID id, AdvertDTO dto, User user, List<MultipartFile> photos) throws IOException;

    void addPhoto(List<MultipartFile> photos, UUID id) throws IOException;

    Advert update(AdvertUpdateDTO advertDTO);

    Advert getAdvertById(UUID id) throws AdvertNotFoundException;

    Advert getAdvertById(UUID id, UserDetailsImpl user) throws AdvertNotFoundException;

    String deleteAdvertById(UUID id) throws AdvertNotFoundException;

    List<Advert> getAllByUser(long userId) throws AdvertNotFoundException;

    boolean existsByTitleAndUsername(String title, String username);

    List<MultipartFile> getMultipartFiles(MultipartFile file1, MultipartFile file2,
                                          MultipartFile file3, MultipartFile file4,
                                          MultipartFile file5, MultipartFile file6,
                                          MultipartFile file7, MultipartFile file8);
}