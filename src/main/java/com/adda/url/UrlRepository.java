package com.adda.url;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The UrlRepository interface {@link Url}
 */

@Repository
public interface UrlRepository extends JpaRepository<Url, Long> {
    Url findByShortLink(String shortLink);
}