package com.restaurant.backend.domains.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "receipt_details")
public class ReceiptDetail {
    @SequenceGenerator(name = "receipt_details_id_gen", sequenceName = "ingredients_ingre_id_seq", allocationSize = 1)
    @EmbeddedId
    private ReceiptDetailId id;

    @MapsId("recId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "rec_id", nullable = false)
    private Receipt rec;

    @MapsId("itemId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "item_id", nullable = false)
    private MenuItem item;

    @NotNull
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @NotNull
    @Column(name = "price", nullable = false, precision = 18, scale = 2)
    private BigDecimal price;

}