package com.adda.advert;

import com.adda.advert.category.Category;
import com.adda.advert.dto.AdvertTransferDTO;
import com.adda.advert.photo.Photo;
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
@Entity
@Table(name = "advertisements")
@NoArgsConstructor
public class Advertisement {

    @Id
    private UUID id;

    @NotBlank
    private String title;

    @Pattern(regexp = "^[0-9]*$",
            message = "Must start and end with digits only")
    private String price;

    @NotBlank
    private String description;

    private String date;

    private Integer viewers = 0;

    private String qrCode;

    @OneToOne(cascade = CascadeType.ALL)
    private Photo photos;

    @ManyToOne
    private Category category;

    @ManyToOne
    private User user;

    public Advertisement(AdvertTransferDTO advertDTO) {
        this.id = advertDTO.getId();
        this.title = advertDTO.getTitle();
        this.price = advertDTO.getPrice();
        this.description = advertDTO.getDescription();
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
        Advertisement that = (Advertisement) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
