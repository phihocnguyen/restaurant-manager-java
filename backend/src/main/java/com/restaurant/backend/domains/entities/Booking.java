package com.restaurant.backend.domains.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bookings_id_gen")
    @SequenceGenerator(name = "bookings_id_gen", sequenceName = "bookings_bk_id_seq", allocationSize = 1)
    @Column(name = "bk_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emp_id")
    private Employee emp;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "cus_id")
    private Customer cus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tab_id")
    private DiningTable tab;

    @NotNull
    @Column(name = "bk_stime", nullable = false)
    private Instant bkStime;

    @NotNull
    @Column(name = "bk_otime", nullable = false)
    private Instant bkOtime;

    @NotNull
    @Column(name = "bk_status", nullable = false)
    private Short bkStatus;

    @NotNull
    @ColumnDefault("false")
    @Column(name = "isdeleted", nullable = false)
    private Boolean isdeleted = false;

}