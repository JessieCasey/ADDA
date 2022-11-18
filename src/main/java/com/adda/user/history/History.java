package com.adda.user.history;

import com.adda.advert.Advertisement;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "history_table")
public class History {
    @Id
    private long id;

    @Column(name = "adverts_id")
    @OneToMany
    private List<Advertisement> adverts = new ArrayList<>();

    public History(long id) {
        this.id = id;
    }

    public History() {
    }


    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
