package com.restaurant.backend.domains.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "stockin")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Stockin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sto_id")
    private Integer id;

    @Column(name = "sto_date")
    private Instant date;

    @Column(name = "sto_price", nullable = false)
    private Double price;

    @OneToMany(mappedBy = "stockin", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StockinDetailsIngre> stockinDetailsIngres;

    @OneToMany(mappedBy = "stockin", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StockinDetailsDrinkOther> stockinDetailsDrinkOthers;
}