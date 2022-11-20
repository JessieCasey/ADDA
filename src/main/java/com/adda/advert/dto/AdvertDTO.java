package com.adda.advert.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;

@Getter
@Setter
public class AdvertDTO {
    private String title;
    @JsonProperty("category_id")
    private Long categoryId;
    private Float price;
    private String description;
}
