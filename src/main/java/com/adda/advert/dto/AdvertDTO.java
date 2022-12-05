package com.adda.advert.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * The AdvertDTO class is required if we want to create the advert entity.
 */
@Getter
@Setter
public class AdvertDTO {
    private String title;
    @JsonProperty("category_id")
    private Long categoryId;
    private Float price;
    private String description;
}
