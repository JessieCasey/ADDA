package com.adda.user.wishlist;

import com.adda.advert.AdvertisementEntity;
import com.adda.advert.dto.AdvertResponseDTO;
import com.adda.user.User;

public interface WishListService {

    WishListEntity getWishList(User user) throws IllegalAccessException;

    AdvertResponseDTO addAdvertToWishList(User user, AdvertisementEntity advertisement) throws IllegalAccessException;

    AdvertResponseDTO deleteAdvertFromWishList(User user, AdvertisementEntity advertisement) throws IllegalAccessException;

    void createWishList(User user);
}
