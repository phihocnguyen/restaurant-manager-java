package com.restaurant.backend.domains.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "dining_tables")
public class DiningTable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dining_tables_id_gen")
    @SequenceGenerator(name = "dining_tables_id_gen", sequenceName = "dining_tables_tab_id_seq", allocationSize = 1)
    @Column(name = "tab_id", nullable = false)
    private Integer id;

    @Column(name = "tab_num")
    private Short tabNum;

    @NotNull
    @Column(name = "tab_status", nullable = false)
    private Boolean tabStatus = false;

    @NotNull
    @ColumnDefault("false")
    @Column(name = "isdeleted", nullable = false)
    private Boolean isdeleted = false;

}