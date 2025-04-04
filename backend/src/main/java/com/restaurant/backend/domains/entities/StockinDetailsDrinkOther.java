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
@Table(name = "stockin_details_drink_other")
public class StockinDetailsDrinkOther {
    @SequenceGenerator(name = "stockin_details_drink_other_id_gen", sequenceName = "stockin_sto_id_seq", allocationSize = 1)
    @EmbeddedId
    private StockinDetailsDrinkOtherId id;

    @MapsId("stoId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "sto_id", nullable = false)
    private Stockin sto;

    @MapsId("itemId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "item_id", nullable = false)
    private MenuItem item;

    @NotNull
    @Column(name = "quantity_units", nullable = false)
    private Integer quantityUnits;

    @NotNull
    @Column(name = "cprice", nullable = false, precision = 18, scale = 2)
    private BigDecimal cprice;

    @ColumnDefault("0")
    @Column(name = "total_cprice", precision = 18, scale = 2)
    private BigDecimal totalCprice;

}