package com.adda.url;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * The Url class which is used to create short link functionality
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Url {

    @Id
    @GeneratedValue
    private long id;

    private String originalUrl;
    private String shortLink;
    private LocalDateTime creationDate;
    private LocalDateTime expirationDate;

}