package com.adda.user.wishlist;

import com.adda.advert.photo.Photo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * The WishListRepository interface {@link WishList}
 */


@Repository
public interface WishListRepository extends CrudRepository<WishList, UUID> {

}
