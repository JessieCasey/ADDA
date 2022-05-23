package com.adda.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
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
}
