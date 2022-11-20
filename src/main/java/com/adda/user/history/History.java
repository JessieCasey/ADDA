package com.adda.user.history;

import com.adda.advert.Advert;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The History entity
 */

@Entity
@Getter
@Setter
@Table(name = "histories")
@NoArgsConstructor
public class History {
    @Id
    private long id;

    @Column(name = "adverts_id")
    @OneToMany
    private List<Advert> adverts = new ArrayList<>();

    public History(long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
