package com.adda.user.updateToken;

import com.adda.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.time.Instant;


/**
 * The UpdateToken entity  is used to update sensitive data (password or email)
 */


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
