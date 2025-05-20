package com.restaurant.backend.domains.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "stockin_details_drink_other")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StockinDetailsDrinkOther {
    @EmbeddedId
    private StockinDetailsDrinkOtherId id;

    @ManyToOne
    @MapsId("stoId")
    @JoinColumn(name = "sto_id")
    private Stockin stockin;

    @ManyToOne
    @MapsId("itemId")
    @JoinColumn(name = "item_id")
    private MenuItem menuItem;

    @Column(name = "quantity_units", nullable = false)
    private Double quantityUnits;

    @Column(name = "cprice", nullable = false, columnDefinition = "NUMERIC(18,2)")
    private Double cPrice;

    @Column(name = "total_cprice", columnDefinition = "NUMERIC(18,2)")
    private Double totalCPrice;
}
