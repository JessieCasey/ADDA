package com.adda.service;

import com.adda.domain.AdvertisementEntity;
import com.adda.domain.UserEntity;
import com.adda.domain.WishListEntity;

public interface WishListService {

    WishListEntity getWishList(UserEntity user);

    String addAdvertToWishList(UserEntity user, AdvertisementEntity advertisement);

    String deleteAdvertFromWishList(UserEntity user, AdvertisementEntity advertisement);

    boolean isWishListCreated(UserEntity user);

    void createWishList(UserEntity user);

    WishListEntity getWishListEntity(long id);
}
