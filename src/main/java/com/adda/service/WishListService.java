package com.adda.service;

import com.adda.domain.AdvertisementEntity;
import com.adda.domain.UserEntity;
import com.adda.domain.WishListEntity;
import com.adda.repository.AdvertisementRepository;
import com.adda.repository.UserRepository;
import com.adda.repository.WishListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class WishListService {
    @Autowired
    private AdvertisementRepository advertisementRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WishListRepository wishListRepository;

    public WishListEntity getWishList(UserEntity user) {
        if (isWishListCreated(user)) {
            return wishListRepository.findById(user.getWishList()).get();
        }
        createWishList(user);

        return wishListRepository.findById(user.getWishList()).get();
    }

    public String addAdvertToWishList(UserEntity user, AdvertisementEntity advertisement) {
        if (!isWishListCreated(user)) {
            createWishList(user);
        }

        WishListEntity wishListEntity = wishListRepository.findById(user.getWishList()).get();
        advertisement.setWishListList(wishListEntity);
        wishListEntity.getAdvertisements().add(advertisement);
        wishListRepository.save(wishListEntity);


        return "Added";
    }

    public String deleteAdvertFromWishList(UserEntity user, AdvertisementEntity advertisement) {
        if (!isWishListCreated(user)) {
            createWishList(user);
        }
        WishListEntity wishListEntity = wishListRepository.findById(user.getWishList()).get();
        //advertisement.setWishListList(null);
        wishListEntity.getAdvertisements().remove(advertisement);
        wishListRepository.save(wishListEntity);
        return "Deleted";

    }

    public boolean isWishListCreated(UserEntity user) {
        if (user.getWishList() != null) {
            return wishListRepository.existsById(user.getWishList());
        }
        return false;
    }


    public void createWishList(UserEntity user) {
        WishListEntity wishListEntity = getWishListEntity(user.getId());
        user.setWishList(wishListEntity.getId());
        wishListRepository.save(wishListEntity);
        userRepository.save(user);
    }

    private WishListEntity getWishListEntity(long id) {
        return new WishListEntity(UUID.randomUUID(), id);
    }
}
