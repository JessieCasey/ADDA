package com.adda.advert.photo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The PhotoRepository interface {@link Photo}
 */

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
}
