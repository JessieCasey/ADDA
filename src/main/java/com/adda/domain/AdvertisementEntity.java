package com.adda.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

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


    public String getGeoposition() {
        return geoposition;
    }

    public void setGeoposition(String geoposition) {
        this.geoposition = geoposition;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(UserEntity user) {
        this.username = user.getUsername();
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public AdvertisementEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGeoPosition() {
        return geoposition;
    }

    public void setGeoPosition(String geoPosition) {
        this.geoposition = geoPosition;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @JsonIgnore
    public PhotoEntity getPhotoLinks() {
        return photoLinks;
    }

    public void setPhotoLinks(PhotoEntity photoLinks) {
        this.photoLinks = photoLinks;
    }
}
