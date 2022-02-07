package com.adda.model;

import com.adda.domain.AdvertisementEntity;

public class Advertisement {
    private Long id;
    private String title;
    private String categories;
    private String description;
    private String geoPosition;
    private String email;
    private String phone;
    private String photos;
    private String username;


    public static Advertisement toModel(AdvertisementEntity entity) {
        Advertisement model = new Advertisement();
        model.setId(entity.getId());
        model.setTitle(entity.getTitle());
        model.setCategories(entity.getCategories());
        model.setDescription(entity.getDescription());
        model.setGeoPosition(entity.getGeoPosition());
        model.setEmail(entity.getEmail());
        model.setPhone(entity.getPhone());
        model.setUsername(entity.getUsername());
        model.setPhotos(entity.getPhotoLinks().toString());

        return model;
    }

    public Advertisement() {
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
        return geoPosition;
    }

    public void setGeoPosition(String geoPosition) {
        this.geoPosition = geoPosition;
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

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
