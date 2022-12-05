package com.adda.services;

import com.adda.AddaApplication;
import com.adda.advert.Advert;
import com.adda.advert.repository.AdvertRepository;
import com.adda.user.User;
import com.adda.user.repository.UserRepository;
import com.adda.user.wishlist.WishList;
import com.adda.user.wishlist.service.WishListService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AddaApplication.class)
public class WishListServiceTests {

    @Autowired
    private WishListService wishListService;

    @Autowired
    private AdvertRepository advertRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void getWishList() {
        WishList wishList = wishListService.getWishList(userRepository.getById(1L));
        assertNotNull(wishList);
    }

    @Test
    public void addAdvertToWishList() throws IllegalAccessException {
        User byId = userRepository.getById(1L);
        Advert advertById = advertRepository.getById(UUID.fromString("73e0b7d7-0051-48d5-b263-3b9bccc20ddb"));
        wishListService.addAdvertToWishList(byId, advertById);
        WishList wishList = wishListService.getWishList(byId);
        assertTrue(wishList.getAdverts().contains(advertById));
    }

    @Test
    public void deleteAdvertFromWishList() throws IllegalAccessException {
        Advert advertById = advertRepository.getById(UUID.fromString("73e0b7d7-0051-48d5-b263-3b9bccc20ddb"));
        User byId = userRepository.getById(1L);
        WishList wishList = wishListService.getWishList(byId);
        if (!wishList.getAdverts().contains(advertById)) {
            wishListService.addAdvertToWishList(byId, advertById);
        }
        wishList = wishListService.getWishList(byId);
        wishList.getAdverts().forEach(System.out::println);
        assertTrue(wishList.getAdverts().contains(advertById));

        wishListService.deleteAdvertFromWishList(byId, advertById);
        wishList = wishListService.getWishList(byId);
        wishList.getAdverts().forEach(System.out::println);
        assertFalse(wishList.getAdverts().contains(advertById));
    }
}
