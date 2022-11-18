package com.adda.advert.dto;

import com.adda.advert.category.CategoriesEntity;
import com.adda.advert.photo.PhotoEntity;
import com.adda.user.User;
import lombok.Getter;

import java.util.UUID;

@Getter
public class AdvertTransferDTO {
    UUID id;
    String title;
    String price;
    String description;
    String email;
    String username;
    PhotoEntity photos;
    CategoriesEntity category;
    User user;
    String date;
    String qrCode;

    public AdvertTransferDTO(UUID id, AdvertisementDTO dto, User user, PhotoEntity photos,
                             CategoriesEntity category, String date, String qrCode) {
        this.id = id;
        this.title = dto.getTitle();
        this.price = dto.getPrice();
        this.description = dto.getDescription();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.photos = photos;
        this.category = category;
        this.user = user;
        this.date = date;
        this.qrCode = qrCode;
    }
}
