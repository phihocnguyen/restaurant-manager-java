package com.restaurant.backend.domains.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "financial_history")
public class FinancialHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "financial_history_id_gen")
    @SequenceGenerator(name = "financial_history_id_gen", sequenceName = "financial_history_fin_id_seq", allocationSize = 1)
    @Column(name = "fin_id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "fin_date", nullable = false)
    private Instant finDate;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 10)
    private FHType type;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "amount", nullable = false, precision = 18, scale = 2)
    private BigDecimal amount;

    @Column(name = "reference_id")
    private Integer referenceId;

    @Size(max = 20)
    @Column(name = "reference_type", length = 20)
    private String referenceType;
}