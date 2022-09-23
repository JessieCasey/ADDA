package com.adda.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@ToString
@Entity
@Table(name = "photo_table")
public class PhotoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String photo1;
    private String photo2;
    private String photo3;
    private String photo4;
    private String photo5;
    private String photo6;
    private String photo7;
    private String photo8;

    @JsonIgnore
    @OneToOne(mappedBy = "photos", cascade = CascadeType.ALL)
    private AdvertisementEntity advert;

    public PhotoEntity() {
        setPhotos(new String[8]);
    }

    public void setPhotos(String[] photos) {
        this.photo1 = photos[0];
        this.photo2 = photos[1];
        this.photo3 = photos[2];
        this.photo4 = photos[3];
        this.photo5 = photos[4];
        this.photo6 = photos[5];
        this.photo7 = photos[6];
        this.photo8 = photos[7];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PhotoEntity that = (PhotoEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
