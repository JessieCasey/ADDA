package com.adda.advert.service;

import com.adda.advert.Advert;
import com.adda.advert.category.CategoryRepository;
import com.adda.advert.dto.AdvertDTO;
import com.adda.advert.dto.AdvertTransferDTO;
import com.adda.advert.dto.AdvertUpdateDTO;
import com.adda.advert.exception.AdvertNotFoundException;
import com.adda.advert.photo.Photo;
import com.adda.advert.photo.service.PhotoServiceImpl;
import com.adda.advert.repository.AdvertRepository;
import com.adda.exception.NullEntityReferenceException;
import com.adda.user.User;
import com.adda.user.exception.UserNotFoundException;
import com.adda.user.history.History;
import com.adda.user.history.HistoryRepository;
import com.adda.user.repository.UserRepository;
import com.adda.user.service.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class AdvertServiceImpl implements AdvertService {

    private final HistoryRepository historyRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final AdvertRepository advertRepository;

    @Autowired
    public AdvertServiceImpl(HistoryRepository historyRepository, UserRepository userRepository,
                             CategoryRepository categoryRepository, AdvertRepository advertRepository) {
        this.historyRepository = historyRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.advertRepository = advertRepository;
    }

    @Override
    public Page<Advert> fetchAdvertsWithFilteringAndSorting(String title, String description, int page, int size, List<String> sortList, String sortOrder) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(createSortOrder(sortList, sortOrder)));
        return advertRepository.findByTitleLikeAndDescriptionLike(title, description, pageable);
    }

    @Override
    public List<Advert> fetchAdvertDataAsList() {
        return advertRepository.findAll();
    }

    @Override
    public List<Advert> fetchFilteredAdvertsAsList(String title, String description) {
        return advertRepository.findByTitleLikeAndDescriptionLike(title, description);
    }

    @Override
    public Page<Advert> fetchAdvertDataAsPageWithFiltering(String title, String description, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return advertRepository.findByTitleLikeAndDescriptionLike(title, description, pageable);
    }

    private List<Sort.Order> createSortOrder(List<String> sortList, String sortDirection) {
        List<Sort.Order> sorts = new ArrayList<>();
        Sort.Direction direction;
        for (String sort : sortList) {
            if (sortDirection != null) {
                direction = Sort.Direction.fromString(sortDirection);
            } else {
                direction = Sort.Direction.DESC;
            }
            sorts.add(new Sort.Order(direction, sort));
        }
        return sorts;
    }

    @Override
    public Advert create(AdvertDTO dto, UserDetailsImpl userDetails, List<MultipartFile> photos) throws IOException {
        return create(null, dto, userRepository.findById(userDetails.getId()).orElseThrow(IllegalArgumentException::new), photos);
    }

    @Override
    public Advert create(UUID id, AdvertDTO dto, User user, List<MultipartFile> photos) throws IOException {
        UUID advertID = Optional.ofNullable(id).orElse(UUID.randomUUID());

        AdvertTransferDTO transferDTO = new AdvertTransferDTO(
                advertID, dto, user,
                new Photo(photos.size()),
                categoryRepository.findById(dto.getCategoryId()).orElseThrow(IllegalArgumentException::new),
                getCurrentTime(),
                "QRcodeServiceImpl.getUrlOfAdvertisement(advertID)"
        );

        Advert saved = advertRepository.save(new Advert(transferDTO));

        addPhoto(photos, saved.getId());
        return saved;
    }

    public String getCurrentTime() {
        return new SimpleDateFormat("dd-MM-yyyy").format(new Date());
    }

    @Override
    public void addPhoto(List<MultipartFile> photos, UUID id) throws IOException {
        Advert advert = advertRepository.findById(id).get();
        advert.setPhotos(PhotoServiceImpl.uploadPhotoToAdvertisement(photos));
        advertRepository.save(advert);
    }

    @Override
    public Advert update(AdvertUpdateDTO advertDTO) {
        Advert advert = getAdvertById(advertDTO.getId());
        if (advert != null) {
            getAdvertById(advert.getId());

            advert.setDescription(advertDTO.getDescription());
            advert.setPrice(advertDTO.getPrice());

            return advertRepository.save(advert);
        }
        throw new NullEntityReferenceException("User cannot be 'null'");
    }

    @Override
    public Advert getAdvertById(UUID id) throws AdvertNotFoundException {
        return getAdvertById(id, null);
    }

    @Override
    public Advert getAdvertById(UUID id, UserDetailsImpl userDetails) throws AdvertNotFoundException {

        Advert advert = advertRepository.findById(id).orElseThrow(() -> new AdvertNotFoundException("Advert is not found"));

        if (userDetails != null) {
            User user = userRepository.findById(userDetails.getId()).orElseThrow(() -> new UserNotFoundException("User is not found"));
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
        Advert advert = getAdvertById(id);
        advertRepository.deleteById(id);
        log.info("Method 'advertService.deleteAdvertById(UUID id)': Advert is deleted from the DB");
        return advert.getTitle();
    }

    @Override
    public List<Advert> getAllByUser(long userId) throws AdvertNotFoundException {
        if (userRepository.findById(userId).isEmpty()) {
            throw new AdvertNotFoundException("The user doesn't have any adverts");
        } else {
            return advertRepository.findAllByUser(userRepository.findById(userId).get());
        }
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
