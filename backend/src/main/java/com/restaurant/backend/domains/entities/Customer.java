package com.restaurant.backend.domains.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customers_id_gen")
    @SequenceGenerator(name = "customers_id_gen", sequenceName = "customers_cus_id_seq", allocationSize = 1)
    @Column(name = "cus_id", nullable = false)
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Column(name = "cus_name", nullable = false, length = 50)
    private String cusName;

    @Size(max = 100)
    @Column(name = "cus_addr", length = 100)
    private String cusAddr;

    @Size(max = 20)
    @Column(name = "cus_phone", length = 20)
    private String cusPhone;

    @Size(max = 12)
    @Column(name = "cus_cccd", length = 12)
    private String cusCccd;

    @Size(max = 50)
    @Column(name = "cus_email", length = 50)
    private String cusEmail;

    @NotNull
    @ColumnDefault("false")
    @Column(name = "isvip", nullable = false)
    private Boolean isvip = false;

    @NotNull
    @ColumnDefault("false")
    @Column(name = "isdeleted", nullable = false)
    private Boolean isdeleted = false;

}