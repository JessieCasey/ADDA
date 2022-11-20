package com.adda.advert.dto;

import com.adda.advert.Advert;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.util.UUID;

@Value
public class AdvertResponseDTO {
    UUID id;
    String title;
    Float price;
    String description;
    String email;
    String username;
    String date;
    Integer viewers;
    @JsonProperty("qr_code_link")
    String qrCode;
    String[] photos;
    String category;

    public AdvertResponseDTO(Advert advert) {
        this.id = advert.getId();
        this.title = advert.getTitle();
        this.price = advert.getPrice();
        this.description = advert.getDescription();
        this.email = advert.getUser().getEmail();
        this.username = advert.getUser().getUsername();
        this.date = advert.getDate();
        this.viewers = advert.getViewers();
        this.qrCode = advert.getQrCode();
        this.photos = advert.getPhotos().getArray();
        this.category = advert.getCategory().getCategoryName();
    }
}
