package com.adda.DTO;

import lombok.Data;

@Data
public class AdvertisementDTO {
    private String title;
    private Long categoryId;
    private Integer price;
    private String description;
}
