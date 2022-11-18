package com.adda.advert;

import com.adda.advert.category.CategoriesRepository;
import com.adda.advert.dto.AdvertTransferDTO;
import com.adda.advert.dto.AdvertisementDTO;
import com.adda.advert.dto.AdvertisementUpdateDTO;
import com.adda.advert.exception.AdvertisementNotFoundException;
import com.adda.advert.filter.AdvertPage;
import com.adda.advert.filter.AdvertSearchCriteria;
import com.adda.advert.photo.PhotoEntity;
import com.adda.advert.photo.service.PhotoServiceImpl;
import com.adda.exception.NullEntityReferenceException;
import com.adda.user.User;
import com.adda.user.UserRepository;
import com.adda.user.history.HistoryEntity;
import com.adda.user.history.HistoryRepository;
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
    public AdvertisementServiceImpl(HistoryRepository historyRepository, UserRepository userRepository,
                                    CategoriesRepository categoriesRepository, AdvertisementRepository advertisementRepository,
                                    AdvertisementCriteriaRepository advertCriteriaRepository) {
        this.historyRepository = historyRepository;
        this.userRepository = userRepository;
        this.categoriesRepository = categoriesRepository;
        this.advertisementRepository = advertisementRepository;
        this.advertCriteriaRepository = advertCriteriaRepository;
    }

    @Override
    public AdvertisementEntity create(AdvertisementDTO dto, User user, List<MultipartFile> photos) throws IOException {
        return create(null, dto, user, photos);
    }

    @Override
    public AdvertisementEntity create(UUID id, AdvertisementDTO dto, User user, List<MultipartFile> photos) throws IOException {
        UUID advertID = Optional.ofNullable(id).orElse(UUID.randomUUID());

        AdvertTransferDTO transferDTO = new AdvertTransferDTO(
                advertID, dto, user,
                new PhotoEntity(photos.size()),
                categoriesRepository.findById(dto.getCategoryId()).orElseThrow(IllegalArgumentException::new),
                getCurrentTime(),
                QRcodeServiceImpl.getUrlOfAdvertisement(advertID)
        );

        AdvertisementEntity saved = advertisementRepository.save(new AdvertisementEntity(transferDTO));
        addPhoto(photos, saved.getId());
        return saved;
    }

    public String getCurrentTime() {
        return new SimpleDateFormat("dd-MM-yyyy").format(new Date());
    }

    @Override
    public void addPhoto(List<MultipartFile> photos, UUID id) throws IOException {
        AdvertisementEntity advert = advertisementRepository.findById(id).get();
        advert.setPhotos(PhotoServiceImpl.uploadPhotoToAdvertisement(photos));
        advertisementRepository.save(advert);
    }

    @Override
    public AdvertisementEntity update(AdvertisementEntity advert, AdvertisementUpdateDTO advertDTO) {
        if (advert != null) {
            getAdvertById(advert.getId());

            advert.setDescription(advertDTO.getDescription());
            advert.setPrice(advertDTO.getPrice());

            return advertisementRepository.save(advert);
        }
        throw new NullEntityReferenceException("User cannot be 'null'");
    }

    @Override
    public AdvertisementEntity getAdvertById(UUID id) throws AdvertisementNotFoundException {
        return getAdvertById(id, null);
    }

    @Override
    public AdvertisementEntity getAdvertById(UUID id, User user) throws AdvertisementNotFoundException {
        AdvertisementEntity advert = advertisementRepository.findById(id).get();
        if (advert == null) {
            throw new AdvertisementNotFoundException("Advert is not found");
        }

        if (user != null) {
            if (!historyRepository.existsByIdAndAdvertsIsContaining(user.getId(), advert)) {
                HistoryEntity historyEntity = new HistoryEntity(user.getId());
                historyEntity.getAdverts().add(advert);
                historyRepository.save(historyEntity);
                advert.setViewers(advert.getViewers() + 1);
                advertisementRepository.save(advert);
            }
        }

        return advert;
    }

    @Override
    @Transactional
    public String deleteAdvertById(UUID id) throws AdvertisementNotFoundException {
        AdvertisementEntity advert = getAdvertById(id);
        advertisementRepository.deleteById(id);
        log.info("Method 'advertService.deleteAdvertById(UUID id)': Advert is deleted from the DB");

        return advert.getTitle();
    }

    @Override
    public List<AdvertisementEntity> getAllByUser(long userId) throws AdvertisementNotFoundException {
        if (userRepository.findById(userId).isEmpty()) {
            throw new AdvertisementNotFoundException("The user doesn't have any adverts");
        } else {
            return advertisementRepository.findAllByUser(userRepository.findById(userId).get());
        }
    }

    public Page<AdvertisementEntity> getAdverts(AdvertPage advertPage,
                                                AdvertSearchCriteria advertSearchCriteria) {

        return advertCriteriaRepository.findAllWithFilters(advertPage, advertSearchCriteria);
    }

    @Override
    public boolean existsByTitleAndUsername(String title, String username) {
        return advertisementRepository.existsByTitleAndUsername(title, username);
    }

    @Override
    public List<MultipartFile> getMultipartFiles(MultipartFile file1, MultipartFile file2,
                                                 MultipartFile file3, MultipartFile file4,
                                                 MultipartFile file5, MultipartFile file6,
                                                 MultipartFile file7, MultipartFile file8) {
        List<MultipartFile> fileList = new ArrayList<>();

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
