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
    private String date;

    private Integer viewers = 0;

    @Column(name = "qr_code")
    private String qrCode;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "photos")
    private PhotoEntity photos;

    @ManyToOne
    @JoinColumn(name = "category")
    private CategoriesEntity category;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wishlist_table_id")
    private WishListEntity wishListList;

    public AdvertisementEntity(UUID id,
                               String title,
                               Integer price,
                               String description,
                               String email,
                               String username,
                               PhotoEntity photos,
                               CategoriesEntity category,
                               UserEntity user,
                               String date,
                               String qrCode
                               ) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.description = description;
        this.email = email;
        this.username = username;
        this.photos = photos;
        this.category = category;
        this.user = user;
        this.date = date;
        this.qrCode = qrCode;
    }


    public PhotoEntity getPhotos() {
        return photos;
    }

    public void setPhotos(PhotoEntity photos) {
        this.photos = photos;
    }
}
