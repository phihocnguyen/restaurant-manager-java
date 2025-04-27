package com.restaurant.backend.domains.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cus_id")
    private Integer id;

    @Column(name = "cus_name", nullable = false, length = 50)
    private String name;

    @Column(name = "cus_addr", length = 100)
    private String address;

    @Column(name = "cus_phone", unique = true, length = 20)
    private String phone;

    @Column(name = "cus_cccd", length = 12)
    private String cccd;

    @Column(name = "cus_email", length = 50)
    private String email;

    @Column(name = "isvip", nullable = false)
    @ColumnDefault("false")
    private Boolean isvip = false;

    @Column(name = "isdeleted", nullable = false)
    @ColumnDefault("false")
    private Boolean isdeleted = false;
}