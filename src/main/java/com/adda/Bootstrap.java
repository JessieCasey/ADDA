package com.adda;

import com.adda.domain.*;
import com.adda.repository.AdvertisementRepository;
import com.adda.repository.CategoriesRepository;
import com.adda.repository.RoleRepository;
import com.adda.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class Bootstrap {

    @Bean
    CommandLineRunner runner(RoleRepository roleRepository, CategoriesRepository categoriesRepository,
                             AdvertisementRepository advertisementRepository, UserRepository userRepository) {
        roleRepository.deleteAll();
        categoriesRepository.deleteAll();
        userRepository.deleteAll();
        advertisementRepository.deleteAll();
        return args -> {
            if (!roleRepository.existsByName("ROLE_ADMIN")) {
                roleRepository.save(new RoleEntity("ROLE_ADMIN"));

                categoriesRepository.saveAll(List.of(
                        new CategoriesEntity("Car"),
                        new CategoriesEntity("Electronic"),
                        new CategoriesEntity("Fashion"),
                        new CategoriesEntity("Real Estate"),
                        new CategoriesEntity("Sport")));

                UserEntity user = new UserEntity();
                user.setId(4);
                user.setFirstName("Alex");
                user.setLastName("White");
                user.setUsername("Heritage");
                user.setEmail("heritageWhite@icloud.com");
                RoleEntity roles = roleRepository.findByName("ROLE_ADMIN").orElseThrow(IllegalArgumentException::new);
                user.setRoles(Collections.singleton(roles));

                userRepository.save(user);

                AdvertisementEntity advert = new AdvertisementEntity();
                advert.setId(UUID.fromString("f96401d2-7f63-4891-aafb-0608919b2a03"));
                advert.setTitle("BMW M5");
                advert.setCategory(categoriesRepository.findByCategoryName("Car"));
                advert.setPrice(67895);
                advert.setDescription("M-packet auto");
                advert.setViewers(0);
                advert.setPhotos(new PhotoEntity());
                advert.setDate(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
                advert.setUsername(user.getUsername());
                advert.setEmail(user.getEmail());
                advert.setUser(user);
                advert.setQrCode("https://i.ibb.co/GQZXmb3/qr-code-f96401d2-7f63-4891-aafb-0608919b2a03.png");

                AdvertisementEntity advert2 = new AdvertisementEntity();
                advert2.setId(UUID.fromString("f96401d2-7f63-4891-aafb-0608919b2a04"));
                advert2.setTitle("Iphone 12 PRO EDITION");
                advert2.setCategory(categoriesRepository.findByCategoryName("Electronic"));
                advert2.setPrice(1300);
                advert2.setDescription("256 GB, GOLD COLOR");
                advert2.setViewers(100);
                advert2.setPhotos(new PhotoEntity());
                advert2.setDate(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
                advert2.setUsername(user.getUsername());
                advert2.setEmail(user.getEmail());
                advert2.setUser(user);
                advert2.setQrCode("https://i.ibb.co/GQZXmb3/qr-code-f96401d2-7f63-4891-aafb-0608919b2a03.png");

                AdvertisementEntity advert3 = new AdvertisementEntity();
                advert3.setId(UUID.fromString("f96401d2-7f63-4891-aafb-0608919b2a05"));
                advert3.setTitle("House in Kiev");
                advert3.setCategory(categoriesRepository.findByCategoryName("Real Estate"));
                advert3.setPrice(100350);
                advert3.setDescription("4-room apartment");
                advert3.setViewers(120);
                advert3.setPhotos(new PhotoEntity());
                advert3.setDate(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
                advert3.setUsername(user.getUsername());
                advert3.setEmail(user.getEmail());
                advert3.setUser(user);
                advert3.setQrCode("https://i.ibb.co/GQZXmb3/qr-code-f96401d2-7f63-4891-aafb-0608919b2a03.png");
                advertisementRepository.saveAll(List.of(advert, advert2, advert3));
            }
        };
    }
}