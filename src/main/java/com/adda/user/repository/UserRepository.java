package com.adda.user.repository;

import com.adda.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    String FILTER_CUSTOMERS_ON_FIRST_NAME_AND_LAST_NAME_QUERY =
            "select b from User b where UPPER(b.firstName) like CONCAT('%',UPPER(?1),'%') and UPPER(b.lastName) like CONCAT('%',UPPER(?2),'%')";

    @Query(FILTER_CUSTOMERS_ON_FIRST_NAME_AND_LAST_NAME_QUERY)
    Page<User> findByFirstNameLikeAndLastNameLike(String firstNameFilter, String lastNameFilter, Pageable pageable);

    @Query(FILTER_CUSTOMERS_ON_FIRST_NAME_AND_LAST_NAME_QUERY)
    List<User> findByFirstNameLikeAndLastNameLike(String firstNameFilter, String lastNameFilter);

    Optional<User> findByVerificationCode(String code);

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    Boolean existsByUsername(String username);
}