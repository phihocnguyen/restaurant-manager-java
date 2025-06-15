package com.restaurant.backend.domains.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bk_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emp_id")
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cus_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tab_id")
    private DiningTable table;

    @Column(name = "bk_stime", nullable = false)
    private Instant startTime;

    @Column(name = "bk_otime", nullable = false)
    private Instant endTime;

    @Column(name = "bk_status", nullable = false)
    private Short status; // 0: PENDING, 1: CONFIRMED, 2: CANCELLED

    @Column(name = "isdeleted", nullable = false)
    private Boolean isdeleted = false;

    @Column(name = "special_request")
    private String specialRequest;
}