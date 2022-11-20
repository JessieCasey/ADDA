package com.adda.user.updateToken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The UpdateTokenRepository interface {@link UpdateToken}
 */

@Repository
public interface UpdateTokenRepository extends JpaRepository<UpdateToken, String> {
}
