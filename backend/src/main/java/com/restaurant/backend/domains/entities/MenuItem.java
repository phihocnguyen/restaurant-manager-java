package com.restaurant.backend.domains.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;

public enum ItemType {
    FOOD,
    DRINK,
    OTHER
}

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "menu_items")
public class MenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "menu_items_id_gen")
    @SequenceGenerator(name = "menu_items_id_gen", sequenceName = "menu_items_item_id_seq", allocationSize = 1)
    @Column(name = "item_id", nullable = false)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Size(max = 10)
    @NotNull
    @Column(name = "item_type", nullable = false, length = 10)
    private ItemType itemType;

    @Size(max = 50)
    @NotNull
    @Column(name = "item_name", nullable = false, length = 50)
    private String itemName;

    @Column(name = "item_img", length = Integer.MAX_VALUE)
    private String itemImg;

    @NotNull
    @Column(name = "item_cprice", nullable = false, precision = 18, scale = 2)
    private BigDecimal itemCprice;

    @NotNull
    @Column(name = "item_sprice", nullable = false, precision = 18, scale = 2)
    private BigDecimal itemSprice;

    @ColumnDefault("0")
    @Column(name = "instock")
    private Double instock;

    @NotNull
    @ColumnDefault("false")
    @Column(name = "isdeleted", nullable = false)
    private Boolean isdeleted = false;

}