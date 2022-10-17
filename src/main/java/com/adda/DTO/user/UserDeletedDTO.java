package com.adda.DTO.user;

import com.adda.domain.UserEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserDeletedDTO extends UserResponseDTO {
    @JsonProperty("deleted_extra_adverts")
    private int deletedAdverts;
    @JsonProperty("deleted_time")
    private LocalDateTime localDateTime;

    public UserDeletedDTO(UserEntity user, int deletedAdverts, LocalDateTime localDateTime) {
        super(user);
        this.deletedAdverts = deletedAdverts;
        this.localDateTime = localDateTime;
    }
}
