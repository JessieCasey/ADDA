package com.adda.user.updateToken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UpdateTokenRepository extends JpaRepository<UpdateToken, String> {
}
