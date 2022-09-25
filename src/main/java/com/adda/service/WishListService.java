package com.adda.service;

import com.adda.DTO.advertisements.AdvertResponseDTO;
import com.adda.domain.AdvertisementEntity;
import com.adda.domain.UserEntity;
import com.adda.domain.WishListEntity;

public interface WishListService {

    WishListEntity getWishList(UserEntity user) throws IllegalAccessException;

    AdvertResponseDTO addAdvertToWishList(UserEntity user, AdvertisementEntity advertisement) throws IllegalAccessException;

    AdvertResponseDTO deleteAdvertFromWishList(UserEntity user, AdvertisementEntity advertisement) throws IllegalAccessException;

    void createWishList(UserEntity user);
}
