package com.adda;

import com.adda.user.role.ERole;
import com.adda.user.role.Role;
import com.adda.user.role.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class Bootstrap {

    @Bean
    CommandLineRunner runner(RoleRepository roleRepository) {
        return args -> {
            if (roleRepository.findByName(ERole.ROLE_USER).isEmpty()) {
                Role role1 = new Role(ERole.ROLE_USER);
                Role role2 = new Role(ERole.ROLE_MODERATOR);
                Role role3 = new Role(ERole.ROLE_ADMIN);
                roleRepository.save(role1);
                roleRepository.save(role2);
                roleRepository.save(role3);
            }
        };
    }
}
