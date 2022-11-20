package com.adda.user.wishlist;

import com.adda.advert.Advert;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * The WishList entity
 */


@Entity
@Table(name = "wishlist")
@Getter
@Setter
public class WishList {
    @Id
    private UUID id;

    @Column(name = "user_id")
    private long userId;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Advert> adverts;

    public WishList() {
    }

    public WishList(UUID id, long userId) {
        this.id = id;
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        WishList that = (WishList) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
