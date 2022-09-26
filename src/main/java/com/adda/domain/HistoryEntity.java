package com.adda.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "history_table")
public class HistoryEntity {
    @Id
    private long id;

    @Column(name = "adverts_id")
    @OneToMany
    private List<AdvertisementEntity> adverts = new ArrayList<>();

    public HistoryEntity(long id) {
        this.id = id;
    }

    public HistoryEntity() {
    }


    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
