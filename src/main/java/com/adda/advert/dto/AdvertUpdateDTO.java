package com.adda.advert.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;
import java.util.UUID;

@Getter
@Setter
public class AdvertUpdateDTO {
    private UUID id;
    private Float price;
    private String description;
}