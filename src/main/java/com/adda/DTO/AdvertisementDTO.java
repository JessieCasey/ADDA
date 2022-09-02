package com.adda.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AdvertisementDTO {
    private String title;
    @JsonProperty("category_id")
    private Long categoryId;
    private Integer price;
    private String description;
}
