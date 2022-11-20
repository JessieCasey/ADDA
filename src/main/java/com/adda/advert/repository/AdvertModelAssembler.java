package com.adda.advert.repository;

import com.adda.advert.Advert;
import com.adda.advert.AdvertController;
import com.adda.user.User;
import com.adda.user.UserController;
import com.adda.user.repository.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

/**
 * This class extends RepresentationModelAssemblerSupport which is required for Pagination.
 * It converts the Advert Entity to User Model.
 */
@Component
public class AdvertModelAssembler extends RepresentationModelAssemblerSupport<Advert, AdvertModel> {
    public AdvertModelAssembler() {
        super(AdvertController.class, AdvertModel.class);
    }

    @Override
    public AdvertModel toModel(Advert entity) {
        AdvertModel model = new AdvertModel();
        model.setCategory(entity.getCategory().getCategoryName());
        model.setEmail(entity.getUser().getEmail());
        model.setUsername(entity.getUser().getUsername());
        model.setPhotos(entity.getPhotos().getArray());
        BeanUtils.copyProperties(entity, model);
        return model;
    }
}