package com.adda.user.wishlist;

import com.adda.advert.Advertisement;
import com.adda.advert.dto.AdvertResponseDTO;
import com.adda.user.User;

public interface WishListService {

    WishList getWishList(User user) throws IllegalAccessException;

    AdvertResponseDTO addAdvertToWishList(User user, Advertisement advertisement) throws IllegalAccessException;

    AdvertResponseDTO deleteAdvertFromWishList(User user, Advertisement advertisement) throws IllegalAccessException;

    void createWishList(User user);
}
