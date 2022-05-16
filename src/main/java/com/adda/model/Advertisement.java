package com.adda.model;

import com.adda.domain.AdvertisementEntity;
import com.adda.domain.PhotoEntity;
import lombok.Data;

import java.util.UUID;

@Data
public class Advertisement {
    private UUID id;
    private String title;
    private String categories;
    private String description;
    private String geoPosition;
    private String email;
    private String phone;
    private PhotoEntity photos;
    private String username;

    public static Advertisement toModel(AdvertisementEntity entity) {
        Advertisement model = new Advertisement();
        model.setId(entity.getId());
        model.setTitle(entity.getTitle());
        model.setCategories(String.valueOf(entity.getCategory()));
        model.setDescription(entity.getDescription());
        model.setEmail(entity.getEmail());
        model.setUsername(entity.getUsername());
        model.setPhotos(entity.getPhotos());

        return model;
    }

    public Advertisement() {
    }


}
