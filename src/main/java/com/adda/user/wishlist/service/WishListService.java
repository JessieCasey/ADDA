package com.adda.user.wishlist.service;

import com.adda.advert.Advert;
import com.adda.advert.dto.AdvertResponseDTO;
import com.adda.url.service.UrlServiceImpl;
import com.adda.user.User;
import com.adda.user.wishlist.WishList;

import java.util.UUID;

/**
 * The WishListService interface is required to create AuthServiceImpl {@link UrlServiceImpl}
 */

public interface WishListService {

    WishList getWishList(User user);

    AdvertResponseDTO addAdvertToWishList(User user, Advert advert) throws IllegalAccessException;

    AdvertResponseDTO deleteAdvertFromWishList(User user, Advert advert) throws IllegalAccessException;

    void createWishList(User user);

    WishList getWishListById(UUID wishListId);

    boolean existById(UUID wishListId);

}
