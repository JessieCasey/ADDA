package com.adda.service.impl;

import com.adda.domain.AdvertisementEntity;
import com.adda.domain.UserEntity;
import com.adda.domain.WishListEntity;
import com.adda.repository.UserRepository;
import com.adda.repository.WishListRepository;
import com.adda.service.WishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
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
        if (isWishListCreated(user)) {
            return wishListRepository.findById(user.getWishList()).orElseThrow(IllegalAccessException::new);
        }
        createWishList(user);
        return wishListRepository.findById(user.getWishList()).orElseThrow(IllegalAccessException::new);
    }

    @Override
    public String addAdvertToWishList(UserEntity user, AdvertisementEntity advertisement) throws IllegalAccessException {
        if (!isWishListCreated(user)) {
            createWishList(user);
        }

        WishListEntity wishListEntity = wishListRepository.findById(user.getWishList()).orElseThrow(IllegalAccessException::new);
        advertisement.setWishListList(wishListEntity);
        wishListEntity.getAdvertisements().add(advertisement);
        wishListRepository.save(wishListEntity);

        return "Added";
    }

    @Override
    public String deleteAdvertFromWishList(UserEntity user, AdvertisementEntity advertisement) throws IllegalAccessException {
        if (!isWishListCreated(user)) {
            createWishList(user);
        }
        WishListEntity wishListEntity = wishListRepository.findById(user.getWishList()).orElseThrow(IllegalAccessException::new);

        List<AdvertisementEntity> advertisements = wishListEntity.getAdvertisements();
        advertisements.remove(advertisement);

        advertisement.setWishListList(null);
        wishListEntity.setAdvertisements(advertisements);
        wishListRepository.save(wishListEntity);

        return "Deleted";

    }

    @Override
    public boolean isWishListCreated(UserEntity user) {
        if (user.getWishList() != null) {
            return wishListRepository.existsById(user.getWishList());
        }
        return false;
    }

    @Override
    public void createWishList(UserEntity user) {
        WishListEntity wishListEntity = getWishListEntity(user.getId());
        user.setWishList(wishListEntity.getId());
        wishListRepository.save(wishListEntity);
        userRepository.save(user);
    }

    @Override
    public WishListEntity getWishListEntity(long id) {
        return new WishListEntity(UUID.randomUUID(), id);
    }
}
