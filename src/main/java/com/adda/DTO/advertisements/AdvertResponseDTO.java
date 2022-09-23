package com.adda.DTO.advertisements;

import com.adda.domain.AdvertisementEntity;
import com.adda.domain.CategoriesEntity;
import com.adda.domain.PhotoEntity;
import lombok.Value;

import java.util.UUID;

@Value
public class AdvertResponseDTO {
    UUID id;
    String title;
    Integer price;
    String description;
    String email;
    String username;
    String date;
    Integer viewers;
    String qrCode;
    PhotoEntity photos;
    CategoriesEntity category;

    public AdvertResponseDTO(AdvertisementEntity advertisement) {
        this.id = advertisement.getId();
        this.title = advertisement.getTitle();
        this.price = advertisement.getPrice();
        this.description = advertisement.getDescription();
        this.email = advertisement.getEmail();
        this.username = advertisement.getUsername();
        this.date = advertisement.getDate();
        this.viewers = advertisement.getViewers();
        this.qrCode = advertisement.getQrCode();
        this.photos = advertisement.getPhotos();
        this.category = advertisement.getCategory();
    }
}
