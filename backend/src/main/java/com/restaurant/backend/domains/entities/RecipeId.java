package com.restaurant.backend.domains.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Embeddable
public class RecipeId implements java.io.Serializable {
    private static final long serialVersionUID = -1572686919702287300L;
    @NotNull
    @Column(name = "item_id", nullable = false)
    private Integer itemId;

    @NotNull
    @Column(name = "ingre_id", nullable = false)
    private Integer ingreId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        RecipeId entity = (RecipeId) o;
        return Objects.equals(this.itemId, entity.itemId) &&
                Objects.equals(this.ingreId, entity.ingreId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId, ingreId);
    }

}