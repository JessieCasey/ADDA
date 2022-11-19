package com.adda.user.wishlist;

import com.adda.advert.Advertisement;
import com.adda.advert.dto.AdvertResponseDTO;
import com.adda.user.User;
import com.adda.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class WishListServiceImpl implements WishListService {

    private final UserRepository userRepository;
    private final WishListRepository wishListRepository;

    @Lazy
    @Autowired
    public WishListServiceImpl(UserRepository userRepository, WishListRepository wishListRepository) {
        this.userRepository = userRepository;
        this.wishListRepository = wishListRepository;
    }

    @Override
    public WishList getWishList(User user) throws IllegalAccessException {
        return wishListRepository.findById(user.getWishList()).orElseThrow(IllegalAccessException::new);
    }

    @Override
    public AdvertResponseDTO addAdvertToWishList(User user, Advertisement advertisement) throws IllegalAccessException {
        WishList wishList = wishListRepository.findById(user.getWishList()).orElseThrow(IllegalAccessException::new);
        wishList.getAdvertisements().add(advertisement);

        wishListRepository.save(wishList);
        userRepository.save(user);

        return new AdvertResponseDTO(advertisement);
    }

    @Override
    public AdvertResponseDTO deleteAdvertFromWishList(User user, Advertisement advertisement) throws IllegalAccessException {
        WishList wishList = wishListRepository.findById(user.getWishList()).orElseThrow(IllegalAccessException::new);

        List<Advertisement> advertisements = wishList.getAdvertisements();
        advertisements.remove(advertisement);

        wishList.setAdvertisements(advertisements);

        wishListRepository.save(wishList);
        userRepository.save(user);

        return new AdvertResponseDTO(advertisement);

    }

    @Override
    public void createWishList(User user) {
        WishList wishList = new WishList(UUID.randomUUID(), user.getId());
        user.setWishList(wishList.getId());
        wishListRepository.save(wishList);
        log.info("Method 'createWishList': WishList is saved to DB: " + user.getUsername());
    }
}
