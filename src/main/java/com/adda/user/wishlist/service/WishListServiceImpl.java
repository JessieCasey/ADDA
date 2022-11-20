package com.adda.user.wishlist.service;

import com.adda.advert.Advert;
import com.adda.advert.dto.AdvertResponseDTO;
import com.adda.user.User;
import com.adda.user.repository.UserRepository;
import com.adda.user.wishlist.WishList;
import com.adda.user.wishlist.WishListNotFoundException;
import com.adda.user.wishlist.WishListRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class WishListServiceImpl implements WishListService {

    private final WishListRepository wishListRepository;

    @Autowired
    public WishListServiceImpl(WishListRepository wishListRepository) {
        this.wishListRepository = wishListRepository;
    }

    @Override
    public WishList getWishList(User user) throws IllegalAccessException {
        return wishListRepository.findById(user.getWishList()).orElseThrow(IllegalAccessException::new);
    }

    @Override
    public AdvertResponseDTO addAdvertToWishList(User user, Advert advert) throws IllegalAccessException {
        WishList wishList = wishListRepository.findById(user.getWishList()).orElseThrow(IllegalAccessException::new);
        wishList.getAdverts().add(advert);

        wishListRepository.save(wishList);

        return new AdvertResponseDTO(advert);
    }

    @Override
    public AdvertResponseDTO deleteAdvertFromWishList(User user, Advert advert) throws IllegalAccessException {
        WishList wishList = wishListRepository.findById(user.getWishList()).orElseThrow(IllegalAccessException::new);

        List<Advert> adverts = wishList.getAdverts();
        adverts.remove(advert);

        wishList.setAdverts(new ArrayList<>());
        wishListRepository.save(wishList);

        return new AdvertResponseDTO(advert);
    }

    @Override
    public void createWishList(User user) {
        WishList wishList = new WishList(UUID.randomUUID(), user.getId());
        user.setWishList(wishList.getId());
        wishListRepository.save(wishList);
        log.info("Method 'createWishList': WishList is saved to DB: " + user.getUsername());
    }

    @Override
    public WishList getWishListById(UUID wishListId) {
        return wishListRepository.findById(wishListId).orElseThrow(() ->
                new WishListNotFoundException("WishList with id '" + wishListId + "' is not found"));
    }

    @Override
    public boolean existById(UUID wishListId) {
        return wishListRepository.existsById(wishListId);
    }
}
