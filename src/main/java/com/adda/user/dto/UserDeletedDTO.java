package com.adda.user.dto;

import com.adda.user.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * The UserDeletedDTO class is required if we want to represent deleted entity.
 */

@Getter
@Setter
public class UserDeletedDTO extends UserResponseDTO {
    @JsonProperty("deleted_extra_adverts")
    private int deletedAdverts;
    @JsonProperty("deleted_time")
    private LocalDateTime localDateTime;

    public UserDeletedDTO(User user, int deletedAdverts, LocalDateTime localDateTime) {
        super(user);
        this.deletedAdverts = deletedAdverts;
        this.localDateTime = localDateTime;
    }
}
