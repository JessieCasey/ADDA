package com.adda.user.wishlist;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface WishListRepository extends CrudRepository<WishListEntity, UUID> {

}
