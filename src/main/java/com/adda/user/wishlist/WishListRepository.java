package com.adda.user.wishlist;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WishListRepository extends CrudRepository<WishList, UUID> {

}
