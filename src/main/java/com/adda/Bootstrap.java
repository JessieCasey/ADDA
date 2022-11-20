package com.adda;

import com.adda.advert.category.Category;
import com.adda.advert.category.CategoryRepository;
import com.adda.user.User;
import com.adda.user.repository.UserRepository;
import com.adda.user.role.ERole;
import com.adda.user.role.Role;
import com.adda.user.role.RoleRepository;
import com.adda.user.wishlist.service.WishListService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class Bootstrap {

    @Bean
    CommandLineRunner runner(RoleRepository roleRepository,
                             UserRepository userRepository,
                             PasswordEncoder passwordEncoder,
                             WishListService wishListService,
                             CategoryRepository categoryRepository) {
        return args -> {

            if (roleRepository.findByName(ERole.ROLE_USER).isEmpty()) {
                categoryRepository.save(new Category("Car"));
                categoryRepository.save(new Category("Electronics"));
                categoryRepository.save(new Category("Rental state"));

                Role save = roleRepository.save(new Role(ERole.ROLE_USER));
                Role save1 = roleRepository.save( new Role(ERole.ROLE_MODERATOR));
                Role save2 = roleRepository.save(new Role(ERole.ROLE_ADMIN));

                User admin = new User();
                admin.setEnabled(true);
                admin.setPassword(passwordEncoder.encode("1"));

                admin.setEmail("admin@gmail.com");
                admin.setFirstName("Admin");
                admin.setLastName("Admin");
                admin.setUsername("Admin");

                User adminSaved = userRepository.save(admin);

                Set<Role> roles = new HashSet<>();
                roles.add(save);
                roles.add(save1);
                roles.add(save2);
                adminSaved.setRoles(roles);
                wishListService.createWishList(adminSaved);
                userRepository.save(adminSaved);

                User user = new User();

                user.setEnabled(true);
                user.setPassword(passwordEncoder.encode("1"));
                user.setEmail("user@gmail.com");
                user.setFirstName("Tony");
                user.setLastName("Paperoni");
                user.setUsername("SimpleUser");

                User userSaved = userRepository.save(user);
                Set<Role> roles2 = new HashSet<>();
                roles2.add(save);
                userSaved.setRoles(roles2);

                wishListService.createWishList(userSaved);
                userRepository.save(userSaved);
            }
        };
    }
}
