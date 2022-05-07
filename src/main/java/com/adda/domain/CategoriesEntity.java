package com.adda.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "categories_table")
public class CategoriesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;
    private String categoryName;


}
