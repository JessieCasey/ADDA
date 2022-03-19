package com.adda.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "advertisement_table")
public class AdvertisementEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String categories;
    private String description;
    private String geoposition;
    private String email;
    private String phone;
    private String username;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "photoLinks_id")
    private PhotoEntity photoLinks;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @JsonIgnore
    public PhotoEntity getPhotoLinks() {
        return photoLinks;
    }

    public void setPhotoLinks(PhotoEntity photoLinks) {
        this.photoLinks = photoLinks;
    }
}
