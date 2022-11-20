package com.adda.advert.repository;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.util.UUID;

/**
 * The AdvertModel class extends the Hateoas Representation Model and is required if we want to convert the Advert
 * Entity to a pagination format
 */

@Getter
@Setter
public class AdvertModel extends RepresentationModel<AdvertModel> {
    private UUID id;
    private String title;
    private Float price;
    private String description;
    private String email;
    private String username;
    private String date;
    private Integer viewers;
    @JsonProperty("qr_code_link")
    private String qrCode;
    private String[] photos;
    private String category;
}
