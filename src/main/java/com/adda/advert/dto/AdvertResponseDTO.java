package com.adda.advert.dto;

import com.adda.advert.AdvertisementEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.util.UUID;

@Value
public class AdvertResponseDTO {
    UUID id;
    String title;
    String price;
    String description;
    String email;
    String username;
    String date;
    Integer viewers;
    @JsonProperty("qr_code_link")
    String qrCode;
    String[] photos;
    String category;

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
        this.photos = advertisement.getPhotos().getArray();
        this.category = advertisement.getCategory().getCategoryName();
    }
}
