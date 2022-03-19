package com.adda.model;

import com.adda.domain.AdvertisementEntity;
import lombok.Data;

@Data
public class Advertisement {
    private Long id;
    private String title;
    private String categories;
    private String description;
    private String geoPosition;
    private String email;
    private String phone;
    private String photos;
    private String username;


    public static Advertisement toModel(AdvertisementEntity entity) {
        Advertisement model = new Advertisement();
        model.setId(entity.getId());
        model.setTitle(entity.getTitle());
        model.setCategories(entity.getCategories());
        model.setDescription(entity.getDescription());
        model.setGeoPosition(entity.getGeoposition());
        model.setEmail(entity.getEmail());
        model.setPhone(entity.getPhone());
        model.setUsername(entity.getUsername());
        model.setPhotos(entity.getPhotoLinks().toString());

        return model;
    }

    public Advertisement() {
    }


}
