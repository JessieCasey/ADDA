package com.adda.domain;

import com.adda.DTO.advertisements.AdvertTransferDTO;
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
@Table(name = "advertisement_table")
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

    @Column(name = "qr_code")
    private String qrCode;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "photos")
    private PhotoEntity photos;

    @ManyToOne
    @JoinColumn(name = "category")
    private CategoriesEntity category;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

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
