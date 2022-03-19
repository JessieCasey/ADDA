package com.adda.model;

import com.adda.domain.UserEntity;

import java.util.ArrayList;
import java.util.List;

public class User {
    private Long id;
    private String username;
    private List<Advertisement> adverts;

    public static User toModel(UserEntity entity) {
        User model = new User();
        model.setId(entity.getId());
        model.setUsername(entity.getUsername());
        //model.setAdverts(entity.getAdverts().stream().map(Advertisement::toModel).collect(Collectors.toList()));
        model.setAdverts(new ArrayList<>());
        return model;
    }

    public User() {
    }

    public List<Advertisement> getAdverts() {
        return adverts;
    }

    public void setAdverts(List<Advertisement> adverts) {
        this.adverts = adverts;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}