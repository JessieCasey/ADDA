package com.adda.advert.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * The AdvertTransferDTO class is required to update advert (If you want to update photos
 * you should check PhotoController method uploadPhotoToAdvert()). {@link com.adda.advert.photo.PhotoController}
 *
 */

@Getter
@Setter
public class AdvertUpdateDTO {
    private UUID id;
    private Float price;
    private String description;
}