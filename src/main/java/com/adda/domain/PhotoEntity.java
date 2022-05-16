package com.adda.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import javax.persistence.*;

@Data
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
    public String toString() {
        return "{" +
                "photo1='" + photo1 + '\'' +
                ", photo2='" + photo2 + '\'' +
                ", photo3='" + photo3 + '\'' +
                ", photo4='" + photo4 + '\'' +
                ", photo5='" + photo5 + '\'' +
                ", photo6='" + photo6 + '\'' +
                ", photo7='" + photo7 + '\'' +
                ", photo8='" + photo8 + '\'' +
                '}';
    }
}
