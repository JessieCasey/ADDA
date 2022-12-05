package com.adda.services;

import com.adda.AddaApplication;
import com.adda.advert.Advert;
import com.adda.advert.dto.AdvertDTO;
import com.adda.advert.dto.AdvertUpdateDTO;
import com.adda.advert.exception.AdvertNotFoundException;
import com.adda.advert.repository.AdvertRepository;
import com.adda.advert.service.AdvertService;
import com.adda.user.User;
import com.adda.user.repository.UserRepository;
import com.adda.user.service.UserDetailsImpl;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AddaApplication.class)
public class AdvertServiceTests {

    @Autowired
    private AdvertService advertService;

    @Autowired
    private AdvertRepository advertRepository;

    @Autowired
    private UserRepository userRepository;

    @SuppressWarnings("unchecked")
    @Test
    public void createAdvertTest() throws IOException {
        AdvertDTO advert = new AdvertDTO();
        advert.setTitle("Audi Q8");
        advert.setDescription("RS Sport, 328 HP");
        advert.setPrice(29500.0f);
        advert.setCategoryId(1L);

        Advert advertisement =
                advertService.create(advert, UserDetailsImpl.build(userRepository.getById(1L)), new ArrayList<>());

        assertNotNull(advertRepository.findById(advertisement.getId()));
        assertEquals(advertRepository.findById(advertisement.getId()).get().getTitle(), "Audi Q8");
    }

    @Test
    @Transactional
    public void updateAdvertTest() {
        AdvertUpdateDTO advertUpdateDTO = new AdvertUpdateDTO();
        advertUpdateDTO.setId(UUID.fromString("46ef9821-5f1f-4927-a98f-1a94f71703eb"));
        advertUpdateDTO.setDescription("Updated description");
        advertUpdateDTO.setPrice(13500f);

        advertService.update(advertUpdateDTO);

        Optional<Advert> after = advertRepository.findById(UUID.fromString("46ef9821-5f1f-4927-a98f-1a94f71703eb"));

        assertEquals("Updated description", after.get().getDescription());
        assertEquals(13500F, after.get().getPrice());
    }

    @Test
    @Transactional
    public void checkIfViewsCounted() {
        User byId = userRepository.findById(1L).get();
        Integer viewersBefore = advertRepository.getById(UUID.fromString("46ef9821-5f1f-4927-a98f-1a94f71703eb")).getViewers();
        Integer viewersAfter = advertService.getAdvertById(UUID.fromString("46ef9821-5f1f-4927-a98f-1a94f71703eb"), UserDetailsImpl.build(byId)).getViewers();

        assertEquals(viewersBefore + 1, viewersAfter);
    }

    @Test
    @Transactional
    public void deleteAdvertTest() {
        advertService.deleteAdvertById(UUID.fromString("e0bbdb63-8cbc-49aa-a442-b7ba6ca20e86"));
        assertFalse(advertRepository.existsById(UUID.fromString("e0bbdb63-8cbc-49aa-a442-b7ba6ca20e86")));
    }


    @Test
    public void getAdvertByIdTest() {
        Throwable t = Assertions.assertThrows(AdvertNotFoundException.class,
                () -> advertService.getAdvertById(UUID.fromString("e0bbdf61-8db1-39aa-a442-b7ba6ca20e90")));

        Assertions.assertEquals("Advert is not found", t.getMessage());
    }
}