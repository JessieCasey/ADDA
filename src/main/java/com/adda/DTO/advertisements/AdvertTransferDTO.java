package com.adda.DTO.advertisements;

import com.adda.domain.CategoriesEntity;
import com.adda.domain.PhotoEntity;
import com.adda.domain.UserEntity;
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
    UserEntity user;
    String date;
    String qrCode;

    public AdvertTransferDTO(UUID id, AdvertisementDTO dto, UserEntity user, PhotoEntity photos,
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
