package com.adda.user.wishlist;

import com.adda.advert.AdvertisementEntity;
import com.adda.advert.dto.AdvertResponseDTO;
import com.adda.user.UserEntity;

public interface WishListService {

    WishListEntity getWishList(UserEntity user) throws IllegalAccessException;

    AdvertResponseDTO addAdvertToWishList(UserEntity user, AdvertisementEntity advertisement) throws IllegalAccessException;

    AdvertResponseDTO deleteAdvertFromWishList(UserEntity user, AdvertisementEntity advertisement) throws IllegalAccessException;

    void createWishList(UserEntity user);
}
