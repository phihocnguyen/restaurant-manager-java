package com.restaurant.backend.domains.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "stockin_details_ingre")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StockinDetailsIngre {
    @EmbeddedId
    private StockinDetailsIngreId id;

    @ManyToOne
    @MapsId("stoId")
    @JoinColumn(name = "sto_id")
    private Stockin stockin;

    @ManyToOne
    @MapsId("ingreId")
    @JoinColumn(name = "ingre_id")
    private Ingredient ingredient;

    @Column(name = "quantity_kg", nullable = false)
    private Double quantityKg;

    @Column(name = "cprice", nullable = false, columnDefinition = "NUMERIC(18,2)")
    private Double cPrice;

    @Column(name = "total_cprice", columnDefinition = "NUMERIC(18,2)")
    private Double totalCPrice;
}