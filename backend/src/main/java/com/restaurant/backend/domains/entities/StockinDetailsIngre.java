package com.restaurant.backend.domains.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "stockin_details_ingre")
public class StockinDetailsIngre {
    @SequenceGenerator(name = "stockin_details_ingre_id_gen", sequenceName = "stockin_sto_id_seq", allocationSize = 1)
    @EmbeddedId
    private StockinDetailsIngreId id;

    @MapsId("stoId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "sto_id", nullable = false)
    private Stockin sto;

    @MapsId("ingreId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "ingre_id", nullable = false)
    private Ingredient ingre;

    @NotNull
    @Column(name = "quantity_kg", nullable = false)
    private Double quantityKg;

    @ColumnDefault("0")
    @Column(name = "total_cprice", precision = 18, scale = 2)
    private BigDecimal totalCprice;

    @NotNull
    @Column(name = "cprice", nullable = false, precision = 18, scale = 2)
    private BigDecimal cprice;

}