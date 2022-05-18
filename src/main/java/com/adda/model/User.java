package com.adda.model;

import com.adda.domain.UserEntity;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class User {
    private Long id;
    private String username;
    private List<Advertisement> adverts;

    public static User toModel(UserEntity entity) {
        User model = new User();
        model.setId(entity.getId());
        model.setUsername(entity.getLastName());
        //model.setAdverts(entity.getAdverts().stream().map(Advertisement::toModel).collect(Collectors.toList()));
        model.setAdverts(new ArrayList<>());
        return model;
    }

    public User() {
    }

}