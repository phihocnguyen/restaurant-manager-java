package com.restaurant.backend.domains.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "stockin")
public class Stockin {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stockin_id_gen")
    @SequenceGenerator(name = "stockin_id_gen", sequenceName = "stockin_sto_id_seq", allocationSize = 1)
    @Column(name = "sto_id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "sto_date", nullable = false)
    private Instant stoDate;

    @NotNull
    @Column(name = "sto_price", nullable = false, precision = 18, scale = 2)
    private BigDecimal stoPrice;

}