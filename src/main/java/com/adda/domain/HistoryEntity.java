package com.adda.domain;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "history_table")
public class HistoryEntity {
    @Id
    private UUID id;

    @Column(name = "user_id")
    private long user;

    public HistoryEntity(UUID id, long user) {
        this.id = id;
        this.user = user;
    }

    public HistoryEntity() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        HistoryEntity that = (HistoryEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
