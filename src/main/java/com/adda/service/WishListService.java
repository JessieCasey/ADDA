package com.adda.service;

import com.adda.domain.AdvertisementEntity;
import com.adda.domain.UserEntity;
import com.adda.domain.WishListEntity;

public interface WishListService {

    WishListEntity getWishList(UserEntity user) throws IllegalAccessException;

    String addAdvertToWishList(UserEntity user, AdvertisementEntity advertisement) throws IllegalAccessException;

    String deleteAdvertFromWishList(UserEntity user, AdvertisementEntity advertisement) throws IllegalAccessException;

    boolean isWishListCreated(UserEntity user);

    void createWishList(UserEntity user);

    WishListEntity getWishListEntity(long id);
}
