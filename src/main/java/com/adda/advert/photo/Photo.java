package com.adda.advert.photo;

import com.adda.advert.Advertisement;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@ToString
@Entity
@Table(name = "photo_table")
public class Photo {
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

    private Integer size;

    @JsonIgnore
    @OneToOne(mappedBy = "photos", cascade = CascadeType.ALL)
    private Advertisement advert;

    public Photo() {
        setPhotos(new String[8]);
    }

    public Photo(Integer size) {
        this();
        this.size = size;
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

    public String[] getArray() {
        String[] array = new String[getSize()];
        if (photo1 != null) {
            array[0] = photo1;
        } else if (photo2 != null) {
            array[1] = photo2;
        } else if (photo3 != null) {
            array[2] = photo3;
        } else if (photo4 != null) {
            array[3] = photo4;
        } else if (photo5 != null) {
            array[4] = photo5;
        } else if (photo6 != null) {
            array[5] = photo6;
        } else if (photo7 != null) {
            array[6] = photo7;
        } else if (photo8 != null) {
            array[7] = photo8;
        }
        return array;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Photo that = (Photo) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
