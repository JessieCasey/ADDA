package com.adda.user.updateToken;

import com.adda.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class UpdateToken {
    @Id
    private String token;

    @OneToOne
    private User user;

    private Instant expiryDate;

    private String sensitiveData;
}
