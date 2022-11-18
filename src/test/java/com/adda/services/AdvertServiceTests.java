package com.adda.services;

import com.adda.advert.dto.AdvertDTO;
import com.adda.advert.dto.AdvertUpdateDTO;
import com.adda.advert.Advertisement;
import com.adda.user.User;
import com.adda.advert.repository.AdvertRepository;
import com.adda.user.UserRepository;
import com.adda.advert.service.AdvertisementService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
public class AdvertServiceTests {

    private final AdvertisementService advertService;
    private final AdvertRepository advertRepository;
    private final UserRepository userRepository;

    @Autowired
    public AdvertServiceTests(AdvertisementService advertService, AdvertRepository advertRepository, UserRepository userRepository) {
        this.advertService = advertService;
        this.advertRepository = advertRepository;
        this.userRepository = userRepository;
    }

    @Test
    @Transactional
    public void createAdvertTest() throws IOException {
        AdvertDTO advert = new AdvertDTO();
        advert.setTitle("Audi Q8");
        advert.setDescription("RS Sport, 328 HP");
        advert.setPrice("29500");
        advert.setCategoryId(1L);

        Advertisement advertisement =
                advertService.create(advert, userRepository.getById(1L), new ArrayList<>());

        assertNotNull(advertRepository.findById(advertisement.getId()));
    }

    @Test
    @Transactional
    public void updateAdvertTest() {
        User byId = userRepository.findById(1L).get();
        Integer viewersBefore = advertRepository.getById(UUID.fromString("f96401d2-7f63-4891-aafb-0608919b2a03")).getViewers();
        Integer viewersAfter = advertService.getAdvertById(UUID.fromString("f96401d2-7f63-4891-aafb-0608919b2a03"), byId).getViewers();

        assertEquals(viewersBefore + 1, viewersAfter);
    }

    @Test
    @Transactional
    public void getAdvertByIdTest() {
        AdvertUpdateDTO updateDTO = new AdvertUpdateDTO();
        updateDTO.setDescription("Nearly 418 HP, It was fully repaired after 100.000 miles");
        updateDTO.setPrice("33219");

        advertService.update(advertRepository.getById(getId()), updateDTO);

        assertEquals(updateDTO.getDescription(), advertRepository.getById(getId()).getDescription());
    }

    @Test
    @Transactional
    public void deleteAdvertTest() {
        advertService.deleteAdvertById(UUID.fromString("f96401d2-7f63-4891-aafb-0608919b2a03"));

        assertFalse(advertRepository.existsById(UUID.fromString("f96401d2-7f63-4891-aafb-0608919b2a03")));
    }

    private static UUID getId() {
        return UUID.fromString("f96401d2-7f63-4891-aafb-0608919b2a03");
    }

}