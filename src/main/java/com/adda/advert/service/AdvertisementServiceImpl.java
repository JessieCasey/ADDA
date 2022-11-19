package com.adda.advert.service;

import com.adda.advert.Advertisement;
import com.adda.advert.QRcodeServiceImpl;
import com.adda.advert.category.CategoryRepository;
import com.adda.advert.dto.AdvertTransferDTO;
import com.adda.advert.dto.AdvertDTO;
import com.adda.advert.dto.AdvertUpdateDTO;
import com.adda.advert.exception.AdvertNotFoundException;
import com.adda.advert.filter.AdvertPage;
import com.adda.advert.filter.AdvertSearchCriteria;
import com.adda.advert.photo.Photo;
import com.adda.advert.photo.service.PhotoServiceImpl;
import com.adda.advert.repository.AdvertCriteriaRepository;
import com.adda.advert.repository.AdvertRepository;
import com.adda.exception.NullEntityReferenceException;
import com.adda.user.User;
import com.adda.user.repository.UserRepository;
import com.adda.user.history.History;
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
    private final CategoryRepository categoryRepository;
    private final AdvertRepository advertRepository;
    private final AdvertCriteriaRepository advertCriteriaRepository;

    @Autowired
    public AdvertisementServiceImpl(HistoryRepository historyRepository, UserRepository userRepository,
                                    CategoryRepository categoryRepository, AdvertRepository advertRepository,
                                    AdvertCriteriaRepository advertCriteriaRepository) {
        this.historyRepository = historyRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.advertRepository = advertRepository;
        this.advertCriteriaRepository = advertCriteriaRepository;
    }

    @Override
    public Advertisement create(AdvertDTO dto, User user, List<MultipartFile> photos) throws IOException {
        return create(null, dto, user, photos);
    }

    @Override
    public Advertisement create(UUID id, AdvertDTO dto, User user, List<MultipartFile> photos) throws IOException {
        UUID advertID = Optional.ofNullable(id).orElse(UUID.randomUUID());

        AdvertTransferDTO transferDTO = new AdvertTransferDTO(
                advertID, dto, user,
                new Photo(photos.size()),
                categoryRepository.findById(dto.getCategoryId()).orElseThrow(IllegalArgumentException::new),
                getCurrentTime(),
                QRcodeServiceImpl.getUrlOfAdvertisement(advertID)
        );

        Advertisement saved = advertRepository.save(new Advertisement(transferDTO));
        addPhoto(photos, saved.getId());
        return saved;
    }

    public String getCurrentTime() {
        return new SimpleDateFormat("dd-MM-yyyy").format(new Date());
    }

    @Override
    public void addPhoto(List<MultipartFile> photos, UUID id) throws IOException {
        Advertisement advert = advertRepository.findById(id).get();
        advert.setPhotos(PhotoServiceImpl.uploadPhotoToAdvertisement(photos));
        advertRepository.save(advert);
    }

    @Override
    public Advertisement update(Advertisement advert, AdvertUpdateDTO advertDTO) {
        if (advert != null) {
            getAdvertById(advert.getId());

            advert.setDescription(advertDTO.getDescription());
            advert.setPrice(advertDTO.getPrice());

            return advertRepository.save(advert);
        }
        throw new NullEntityReferenceException("User cannot be 'null'");
    }

    @Override
    public Advertisement getAdvertById(UUID id) throws AdvertNotFoundException {
        return getAdvertById(id, null);
    }

    @Override
    public Advertisement getAdvertById(UUID id, User user) throws AdvertNotFoundException {
        Advertisement advert = advertRepository.findById(id).get();
        if (advert == null) {
            throw new AdvertNotFoundException("Advert is not found");
        }

        if (user != null) {
            if (!historyRepository.existsByIdAndAdvertsIsContaining(user.getId(), advert)) {
                History history = new History(user.getId());
                history.getAdverts().add(advert);
                historyRepository.save(history);
                advert.setViewers(advert.getViewers() + 1);
                advertRepository.save(advert);
            }
        }

        return advert;
    }

    @Override
    @Transactional
    public String deleteAdvertById(UUID id) throws AdvertNotFoundException {
        Advertisement advert = getAdvertById(id);
        advertRepository.deleteById(id);
        log.info("Method 'advertService.deleteAdvertById(UUID id)': Advert is deleted from the DB");

        return advert.getTitle();
    }

    @Override
    public List<Advertisement> getAllByUser(long userId) throws AdvertNotFoundException {
        if (userRepository.findById(userId).isEmpty()) {
            throw new AdvertNotFoundException("The user doesn't have any adverts");
        } else {
            return advertRepository.findAllByUser(userRepository.findById(userId).get());
        }
    }

    public Page<Advertisement> getAdverts(AdvertPage advertPage,
                                          AdvertSearchCriteria advertSearchCriteria) {

        return advertCriteriaRepository.findAllWithFilters(advertPage, advertSearchCriteria);
    }

    @Override
    public boolean existsByTitleAndUsername(String title, String username) {
        return advertRepository.existsByTitleAndUser_Username(title, username);
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
