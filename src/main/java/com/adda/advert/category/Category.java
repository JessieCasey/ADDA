package com.adda.advert.category;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

/**
 * The Category class is required if we want to create the advert entity.
 * DB already has several of categories (check second migration 'V2__INSERT_ROLES_AND_CATEGORIES.sql')
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    private String categoryName;

    public Category(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Category that = (Category) o;
        return categoryId != null && Objects.equals(categoryId, that.categoryId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
