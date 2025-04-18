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
public class StockinDetailsIngreId implements java.io.Serializable {
    private static final long serialVersionUID = -1866692388453273586L;
    @NotNull
    @Column(name = "sto_id", nullable = false)
    private Integer stoId;

    @NotNull
    @Column(name = "ingre_id", nullable = false)
    private Integer ingreId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        StockinDetailsIngreId entity = (StockinDetailsIngreId) o;
        return Objects.equals(this.stoId, entity.stoId) &&
                Objects.equals(this.ingreId, entity.ingreId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stoId, ingreId);
    }

}