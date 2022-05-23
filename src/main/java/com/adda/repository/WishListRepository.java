package com.adda.repository;

import com.adda.domain.WishListEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface WishListRepository extends CrudRepository<WishListEntity, UUID> {

}
