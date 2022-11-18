package com.adda.advert.repository;

import com.adda.advert.Advertisement;
import com.adda.advert.category.Category;
import com.adda.user.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface AdvertRepository extends CrudRepository<Advertisement, UUID> {

    Advertisement getById(UUID id);

    void deleteById(UUID id);


    boolean existsByTitleAndUser_Username(String advertisementName, String username);

    List<Advertisement> findAllByCategory(Category category);

    List<Advertisement> findAllByUser(User user);

}
