package com.adda.domain;

import javax.persistence.*;

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

    @OneToOne(mappedBy = "photoLinks", cascade = CascadeType.ALL)
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

    public Long getId() {
        return id;
    }

    public String getPhoto1() {
        return photo1;
    }

    public void setPhoto1(String photo1) {
        this.photo1 = photo1;
    }

    public String getPhoto2() {
        return photo2;
    }

    public void setPhoto2(String photo2) {
        this.photo2 = photo2;
    }

    public String getPhoto3() {
        return photo3;
    }

    public void setPhoto3(String photo3) {
        this.photo3 = photo3;
    }

    public String getPhoto4() {
        return photo4;
    }

    public void setPhoto4(String photo4) {
        this.photo4 = photo4;
    }

    public String getPhoto5() {
        return photo5;
    }

    public void setPhoto5(String photo5) {
        this.photo5 = photo5;
    }

    public String getPhoto6() {
        return photo6;
    }

    public void setPhoto6(String photo6) {
        this.photo6 = photo6;
    }

    public String getPhoto7() {
        return photo7;
    }

    public void setPhoto7(String photo7) {
        this.photo7 = photo7;
    }

    public String getPhoto8() {
        return photo8;
    }

    public void setPhoto8(String photo8) {
        this.photo8 = photo8;
    }

    public AdvertisementEntity getAdvert() {
        return advert;
    }

    public void setAdvert(AdvertisementEntity advert) {
        this.advert = advert;
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
