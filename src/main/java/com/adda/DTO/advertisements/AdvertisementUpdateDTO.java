package com.adda.DTO.advertisements;

import lombok.Getter;

import javax.validation.constraints.Pattern;

@Getter
public class AdvertisementUpdateDTO {
    @Pattern(regexp = "^[0-9]*$",
            message = "Must start and end with digits only")
    private String price;
    private String description;
}