package com.adda.user.wishlist;

import com.adda.advert.AdvertisementEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "wishlist_table")
@Getter
@Setter
public class WishListEntity {
    @Id
    private UUID id;

    @Column(name = "user_id")
    private long userId;

    @OneToMany(cascade = CascadeType.ALL)
    private List<AdvertisementEntity> advertisements;

    public WishListEntity() {
    }

    public WishListEntity(UUID id, long userId) {
        this.id = id;
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        WishListEntity that = (WishListEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
