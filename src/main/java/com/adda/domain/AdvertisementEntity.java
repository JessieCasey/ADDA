package com.adda.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "advertisement_table")
public class AdvertisementEntity {
    @Id
    private UUID id;
    private String title;
    private Integer price;
    private String description;
    private String email;
    private String username;
    // назва, категорія, ціна, фотки, опис
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "photos")
    private PhotoEntity photos;

    @ManyToOne
    @JoinColumn(name = "category")
    private CategoriesEntity category;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public AdvertisementEntity(UUID id,
                               String title,
                               Integer price,
                               String description,
                               String email,
                               String username,
                               PhotoEntity photos,
                               CategoriesEntity category,
                               UserEntity user) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.description = description;
        this.email = email;
        this.username = username;
        this.photos = photos;
        this.category = category;
        this.user = user;
    }


    public PhotoEntity getPhotos() {
        return photos;
    }

    public void setPhotos(PhotoEntity photos) {
        this.photos = photos;
    }
}
