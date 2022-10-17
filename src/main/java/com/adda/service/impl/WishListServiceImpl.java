package com.adda.service.impl;

import com.adda.DTO.advertisements.AdvertResponseDTO;
import com.adda.domain.AdvertisementEntity;
import com.adda.domain.UserEntity;
import com.adda.domain.WishListEntity;
import com.adda.repository.UserRepository;
import com.adda.repository.WishListRepository;
import com.adda.service.WishListService;
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
    public WishListEntity getWishList(UserEntity user) throws IllegalAccessException {
        return wishListRepository.findById(user.getWishList()).orElseThrow(IllegalAccessException::new);
    }

    @Override
    public AdvertResponseDTO addAdvertToWishList(UserEntity user, AdvertisementEntity advertisement) throws IllegalAccessException {
        WishListEntity wishListEntity = wishListRepository.findById(user.getWishList()).orElseThrow(IllegalAccessException::new);
        wishListEntity.getAdvertisements().add(advertisement);

        wishListRepository.save(wishListEntity);
        userRepository.save(user);

        return new AdvertResponseDTO(advertisement);
    }

    @Override
    public AdvertResponseDTO deleteAdvertFromWishList(UserEntity user, AdvertisementEntity advertisement) throws IllegalAccessException {
        WishListEntity wishListEntity = wishListRepository.findById(user.getWishList()).orElseThrow(IllegalAccessException::new);

        List<AdvertisementEntity> advertisements = wishListEntity.getAdvertisements();
        advertisements.remove(advertisement);

        wishListEntity.setAdvertisements(advertisements);

        wishListRepository.save(wishListEntity);
        userRepository.save(user);

        return new AdvertResponseDTO(advertisement);

    }

    @Override
    public void createWishList(UserEntity user) {
        WishListEntity wishListEntity = new WishListEntity(UUID.randomUUID(), user.getId());
        user.setWishList(wishListEntity.getId());
        wishListRepository.save(wishListEntity);
        log.info("Method 'createWishList': WishList is saved to DB: " + user.getUsername());
    }
}
