package com.adda.advert.dto;

import com.adda.advert.category.Category;
import com.adda.advert.photo.Photo;
import com.adda.user.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

/**
 * The AdvertTransferDTO class is required to collect related entities into one DTO
 * {@link User} {@link Category} {@link Photo}
 */

@Getter
public class AdvertTransferDTO {
    UUID id;
    String title;
    Float price;
    String description;
    String email;
    String username;
    Photo photos;
    Category category;
    User user;
    String date;
    String qrCode;

    public AdvertTransferDTO(UUID id, AdvertDTO dto, User user, Photo photos,
                             Category category, String date, String qrCode) {
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
