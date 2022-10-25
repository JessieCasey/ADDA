package com.adda.advert.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;

@Getter
@Setter
public class AdvertisementDTO {
    @Pattern(regexp = "[A-Z][a-z]+",
            message = "Must start with a capital letter followed by one or more lowercase letters")
    private String title;
    @JsonProperty("category_id")
    private Long categoryId;
    @Pattern(regexp = "^[0-9]*$",
            message = "Must start and end with digits only")
    private String price;
    @Pattern(regexp = "[A-Z][a-z]+",
            message = "Must start with a capital letter followed by one or more lowercase letters")
    private String description;
}
