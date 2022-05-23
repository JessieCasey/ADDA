package com.adda.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "wishlist_table")
@Data
public class WishListEntity {
    @Id
    private UUID id;

    @Column(name = "user_id")
    private long userId;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "wishListList")
    private List<AdvertisementEntity> advertisements;

    public WishListEntity() {

    }

    public WishListEntity(UUID id, long userId) {
        this.id = id;
        this.userId = userId;
    }

}
