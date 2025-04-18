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
public class ReceiptDetailId implements java.io.Serializable {
    private static final long serialVersionUID = -8270632624447409529L;
    @NotNull
    @Column(name = "rec_id", nullable = false)
    private Integer recId;

    @NotNull
    @Column(name = "item_id", nullable = false)
    private Integer itemId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ReceiptDetailId entity = (ReceiptDetailId) o;
        return Objects.equals(this.itemId, entity.itemId) &&
                Objects.equals(this.recId, entity.recId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId, recId);
    }

}