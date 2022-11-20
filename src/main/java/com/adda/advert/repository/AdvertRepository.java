package com.adda.advert.repository;

import com.adda.advert.Advert;
import com.adda.advert.photo.Photo;
import com.adda.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

/**
 * The AdvertRepository interface {@link Advert}
 */

public interface AdvertRepository extends CrudRepository<Advert, UUID> {

    String FILTER_CUSTOMERS_ON_TITLE_AND_PRICE_QUERY =
            "select b from Advert b where UPPER(b.title) like CONCAT('%',UPPER(?1),'%') and UPPER(b.description) like CONCAT('%',UPPER(?2),'%')";

    @Query(FILTER_CUSTOMERS_ON_TITLE_AND_PRICE_QUERY)
    Page<Advert> findByTitleLikeAndDescriptionLike(String title, String description, Pageable pageable);

    @Query(FILTER_CUSTOMERS_ON_TITLE_AND_PRICE_QUERY)
    List<Advert> findByTitleLikeAndDescriptionLike(String title, String description);

    Advert getById(UUID id);

    void deleteById(UUID id);

    boolean existsByTitleAndUser_Username(String advertisementName, String username);

    List<Advert> findAllByUser(User user);

    List<Advert> findAll();

}
