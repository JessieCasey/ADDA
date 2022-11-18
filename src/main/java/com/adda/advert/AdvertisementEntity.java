package com.adda.advert;

import com.adda.advert.category.CategoriesEntity;
import com.adda.advert.dto.AdvertTransferDTO;
import com.adda.advert.photo.PhotoEntity;
import com.adda.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "advertisements")
public class AdvertisementEntity {
    @Id
    private UUID id;
    @NotBlank
    private String title;

    @Pattern(regexp = "^[0-9]*$",
            message = "Must start and end with digits only")
    private String price;

    @NotBlank
    private String description;

    private String email;
    private String username;
    private String date;

    private Integer viewers = 0;

    private String qrCode;

    @OneToOne(cascade = CascadeType.ALL)
    private PhotoEntity photos;

    @ManyToOne
    private CategoriesEntity category;

    @ManyToOne
    private User user;

    public AdvertisementEntity(AdvertTransferDTO advertDTO) {
        this.id = advertDTO.getId();
        this.title = advertDTO.getTitle();
        this.price = advertDTO.getPrice();
        this.description = advertDTO.getDescription();
        this.email = advertDTO.getEmail();
        this.username = advertDTO.getUsername();
        this.photos = advertDTO.getPhotos();
        this.category = advertDTO.getCategory();
        this.user = advertDTO.getUser();
        this.date = advertDTO.getDate();
        this.qrCode = advertDTO.getQrCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AdvertisementEntity that = (AdvertisementEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
