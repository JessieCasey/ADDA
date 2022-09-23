package com.adda.service.impl;

import com.adda.DTO.FilterDTO;
import com.adda.DTO.advertisements.AdvertTransferDTO;
import com.adda.DTO.advertisements.AdvertisementDTO;
import com.adda.domain.AdvertisementEntity;
import com.adda.domain.HistoryEntity;
import com.adda.domain.PhotoEntity;
import com.adda.domain.UserEntity;
import com.adda.exception.AdvertisementNotFoundException;
import com.adda.model.AdvertPage;
import com.adda.model.AdvertSearchCriteria;
import com.adda.repository.AdvertisementRepository;
import com.adda.repository.CategoriesRepository;
import com.adda.repository.HistoryRepository;
import com.adda.repository.UserRepository;
import com.adda.repository.advertisements.AdvertisementCriteriaRepository;
import com.adda.service.AdvertisementService;
import com.adda.service.photoService.PhotoServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class AdvertisementServiceImpl implements AdvertisementService {

    private final HistoryRepository historyRepository;
    private final UserRepository userRepository;
    private final CategoriesRepository categoriesRepository;
    private final AdvertisementRepository advertisementRepository;
    private final AdvertisementCriteriaRepository advertCriteriaRepository;
    @Autowired
    public AdvertisementServiceImpl(HistoryRepository historyRepository, UserRepository userRepository, CategoriesRepository categoriesRepository, AdvertisementRepository advertisementRepository, AdvertisementCriteriaRepository advertCriteriaRepository) {
        this.historyRepository = historyRepository;
        this.userRepository = userRepository;
        this.categoriesRepository = categoriesRepository;
        this.advertisementRepository = advertisementRepository;
        this.advertCriteriaRepository = advertCriteriaRepository;
    }

    @Override
    public AdvertisementEntity create(AdvertisementDTO dto, UserEntity user, List<MultipartFile> photos) throws IOException {
        return create(null, dto, user, photos);
    }

    @Override
    public AdvertisementEntity create(UUID id, AdvertisementDTO dto, UserEntity user, List<MultipartFile> photos) throws IOException {
        UUID advertID = Optional.ofNullable(id).orElse(UUID.randomUUID());
        AdvertTransferDTO transferDTO = new AdvertTransferDTO(
                advertID,
                dto,
                user,
                new PhotoEntity(),
                categoriesRepository.findById(dto.getCategoryId()).orElseThrow(IllegalArgumentException::new),
                getCurrentTime(),
                QRcodeServiceImpl.getUrlOfAdvertisement(advertID)
        );
        log.warn(transferDTO.getId() + "");
        AdvertisementEntity saved = advertisementRepository.save(new AdvertisementEntity(transferDTO));
        addPhoto(photos, saved.getId());
        log.warn("HERE" + "");
        return saved;
    }

    public String getCurrentTime() {
        return new SimpleDateFormat("dd-MM-yyyy").format(new Date());
    }

    @Override
    public void addPhoto(List<MultipartFile> photos, UUID id) throws IOException {
        AdvertisementEntity advert = advertisementRepository.findById(id);
        advert.setPhotos(PhotoServiceImpl.uploadPhotoToAdvertisement(photos));
        advertisementRepository.save(advert);
    }

    @Override
    public AdvertisementEntity getAdvertById(UUID id, UserEntity user) throws AdvertisementNotFoundException {
        AdvertisementEntity advert = advertisementRepository.findById(id);
        if (user != null) {
            if (!historyRepository.existsByIdAndUser(id, user.getId())) {
                HistoryEntity historyEntity = new HistoryEntity(id, user.getId());
                historyRepository.save(historyEntity);
                advert.setViewers(advert.getViewers() + 1);
                advertisementRepository.save(advert);
            }
        }

        if (advert == null) {
            throw new AdvertisementNotFoundException("Advert is not found");
        } else {
            return advert;
        }
    }

    @Override
    @Transactional
    public String deleteAdvertById(UUID id) throws AdvertisementNotFoundException {
        AdvertisementEntity advert = getAdvertById(id, null);
        advertisementRepository.deleteById(id);

        return advert.getTitle();
    }

    @Override
    public List<AdvertisementEntity> getAdvertsByCategory(Long categoryId) throws AdvertisementNotFoundException {
        List<AdvertisementEntity> adverts = advertisementRepository
                .findAllByCategory(categoriesRepository.findById(categoryId).orElseThrow(IllegalArgumentException::new));

        if (adverts == null || (!adverts.stream().findAny().isPresent())) {
            throw new AdvertisementNotFoundException("No adverts in that category");
        }
        return adverts;
    }

    @Override
    public List<AdvertisementEntity> getAdvertsByFilters(FilterDTO filterDTO) throws AdvertisementNotFoundException {
        List<AdvertisementEntity> adverts = null;
        if (filterDTO.getCategoryName() == null || filterDTO.getCategoryName().equals("")) {
            adverts = advertisementRepository.findAllByPriceBetween(filterDTO.getStartPrice(), filterDTO.getEndPrice());
        } else {
            adverts = advertisementRepository.findAllByPriceBetweenAndCategory(filterDTO.getStartPrice(), filterDTO.getEndPrice(), categoriesRepository.findByCategoryName(filterDTO.getCategoryName()));
        }

        if (adverts == null) {
            throw new AdvertisementNotFoundException("No adverts in that category");
        } else {
            return adverts;
        }

    }

    @Override
    public List<AdvertisementEntity> getAllByUser(long userId) throws AdvertisementNotFoundException {
        List<AdvertisementEntity> adverts = null;
        if (!userRepository.findById(userId).isPresent()) {
            throw new AdvertisementNotFoundException("The user doesn't have any adverts");
        } else {
            adverts = advertisementRepository.findAllByUser(userRepository.findById(userId).get());
            if (adverts != null) {
                return adverts;
            } else {
                throw new AdvertisementNotFoundException("The user doesn't have any adverts");
            }
        }
    }

    @Override
    public List<AdvertisementEntity> getAll() {
        Iterable<AdvertisementEntity> source = advertisementRepository.findAll();
        return new ArrayList<>((Collection<? extends AdvertisementEntity>) source);
    }

    public Page<AdvertisementEntity> getAdverts(AdvertPage advertPage,
                                                AdvertSearchCriteria advertSearchCriteria) {
        return advertCriteriaRepository.findAllWithFilters(advertPage, advertSearchCriteria);
    }


    @Override
    public List<MultipartFile> getMultipartFiles(MultipartFile file1, MultipartFile file2,
                                                 MultipartFile file3, MultipartFile file4,
                                                 MultipartFile file5, MultipartFile file6,
                                                 MultipartFile file7, MultipartFile file8) {
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
