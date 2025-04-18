package com.restaurant.backend.domains.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "ingredients")
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ingredients_id_gen")
    @SequenceGenerator(name = "ingredients_id_gen", sequenceName = "ingredients_ingre_id_seq", allocationSize = 1)
    @Column(name = "ingre_id", nullable = false)
    private Integer id;

    @Size(max = 30)
    @NotNull
    @Column(name = "ingre_name", nullable = false, length = 30)
    private String ingreName;

    @NotNull
    @Column(name = "instock_kg", nullable = false)
    private Double instockKg;

    @ColumnDefault("0")
    @Column(name = "ingre_price", precision = 18, scale = 2)
    private BigDecimal ingrePrice;

    @NotNull
    @Column(name = "isdeleted", nullable = false)
    private Boolean isdeleted = false;

}