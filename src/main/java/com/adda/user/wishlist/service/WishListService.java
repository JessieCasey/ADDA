package com.adda.user.wishlist.service;

import com.adda.advert.Advert;
import com.adda.advert.dto.AdvertResponseDTO;
import com.adda.user.User;
import com.adda.user.wishlist.WishList;

import java.util.UUID;

public interface WishListService {

    WishList getWishList(User user) throws IllegalAccessException;

    AdvertResponseDTO addAdvertToWishList(User user, Advert advert) throws IllegalAccessException;

    AdvertResponseDTO deleteAdvertFromWishList(User user, Advert advert) throws IllegalAccessException;

    void createWishList(User user);

    WishList getWishListById(UUID wishListId);

    boolean existById(UUID wishListId);

}
