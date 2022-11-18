package com.adda.user.wishlist;

import com.adda.advert.AdvertisementEntity;
import com.adda.advert.dto.AdvertResponseDTO;
import com.adda.user.User;
import com.adda.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class WishListServiceImpl implements WishListService {

    private final UserRepository userRepository;
    private final WishListRepository wishListRepository;

    @Lazy
    @Autowired
    public WishListServiceImpl(UserRepository userRepository, WishListRepository wishListRepository) {
        this.userRepository = userRepository;
        this.wishListRepository = wishListRepository;
    }

    @Override
    public WishListEntity getWishList(User user) throws IllegalAccessException {
        return wishListRepository.findById(user.getWishList()).orElseThrow(IllegalAccessException::new);
    }

    @Override
    public AdvertResponseDTO addAdvertToWishList(User user, AdvertisementEntity advertisement) throws IllegalAccessException {
        WishListEntity wishListEntity = wishListRepository.findById(user.getWishList()).orElseThrow(IllegalAccessException::new);
        wishListEntity.getAdvertisements().add(advertisement);

        wishListRepository.save(wishListEntity);
        userRepository.save(user);

        return new AdvertResponseDTO(advertisement);
    }

    @Override
    public AdvertResponseDTO deleteAdvertFromWishList(User user, AdvertisementEntity advertisement) throws IllegalAccessException {
        WishListEntity wishListEntity = wishListRepository.findById(user.getWishList()).orElseThrow(IllegalAccessException::new);

        List<AdvertisementEntity> advertisements = wishListEntity.getAdvertisements();
        advertisements.remove(advertisement);

        wishListEntity.setAdvertisements(advertisements);

        wishListRepository.save(wishListEntity);
        userRepository.save(user);

        return new AdvertResponseDTO(advertisement);

    }

    @Override
    public void createWishList(User user) {
        WishListEntity wishListEntity = new WishListEntity(UUID.randomUUID(), user.getId());
        user.setWishList(wishListEntity.getId());
        wishListRepository.save(wishListEntity);
        log.info("Method 'createWishList': WishList is saved to DB: " + user.getUsername());
    }
}
