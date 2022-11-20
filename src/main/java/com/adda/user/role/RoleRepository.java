package com.adda.user.role;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The RoleRepository interface {@link Role}
 */

@Repository
public interface RoleRepository extends CrudRepository<Role, String> {
    Optional<Role> findByName(ERole name);
}