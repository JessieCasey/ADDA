package com.adda.DTO.advertisements;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdvertisementDTO {
    private String title;
    @JsonProperty("category_id")
    private Long categoryId;
    private Integer price;
    private String description;
}
