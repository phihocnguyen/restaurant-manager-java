package com.restaurant.backend.domains.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "employees")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emp_id")
    private Integer id;

    @Column(name = "emp_name", nullable = false, length = 50)
    private String name;

    @Column(name = "emp_addr", length = 100)
    private String address;

    @Column(name = "emp_phone", nullable = false, length = 20)
    private String phone;

    @Column(name = "emp_cccd", unique = true, length = 12)
    private String cccd;

    @Column(name = "emp_role", length = 50)
    private String role;

    @Column(name = "emp_sdate")
    private Instant startDate;

    @Column(name = "emp_worked_days")
    private Integer workedDays;

    @Column(name = "emp_salary", nullable = false, precision = 18, scale = 2)
    private BigDecimal salary;

    @Column(name = "isdeleted", nullable = false)
    @ColumnDefault("false")
    private Boolean isdeleted = false;
}