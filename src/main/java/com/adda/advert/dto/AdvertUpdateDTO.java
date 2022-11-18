package com.adda.advert.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;

@Getter
@Setter
public class AdvertUpdateDTO {
    @Pattern(regexp = "^[0-9]*$",
            message = "Must start and end with digits only")
    private String price;
    private String description;
}