package com.restaurant.backend.domains.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "recipes")
public class Recipe {
    @SequenceGenerator(name = "recipes_id_gen", sequenceName = "receipts_rec_id_seq", allocationSize = 1)
    @EmbeddedId
    private RecipeId id;

    @MapsId("itemId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "item_id", nullable = false)
    private MenuItem item;

    @MapsId("ingreId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "ingre_id", nullable = false)
    private Ingredient ingre;

    @NotNull
    @Column(name = "ingre_quantity_kg", nullable = false)
    private Double ingreQuantityKg;

    @Column(name = "recipe_img", length = Integer.MAX_VALUE)
    private String recipeImg;
}