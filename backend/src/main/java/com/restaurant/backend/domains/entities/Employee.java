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
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employees_id_gen")
    @SequenceGenerator(name = "employees_id_gen", sequenceName = "employees_emp_id_seq", allocationSize = 1)
    @Column(name = "emp_id", nullable = false)
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Column(name = "emp_name", nullable = false, length = 50)
    private String empName;

    @Size(max = 100)
    @Column(name = "emp_addr", length = 100)
    private String empAddr;

    @Size(max = 20)
    @NotNull
    @Column(name = "emp_phone", nullable = false, length = 20)
    private String empPhone;

    @Size(max = 12)
    @Column(name = "emp_cccd", length = 12)
    private String empCccd;

    @Size(max = 50)
    @Column(name = "emp_role", length = 50)
    private String empRole;

    @Column(name = "emp_sdate")
    private Instant empSdate;

    @Column(name = "emp_worked_days")
    private Integer empWorkedDays;

    @NotNull
    @Column(name = "emp_salary", nullable = false, precision = 18, scale = 2)
    private BigDecimal empSalary;

    @NotNull
    @ColumnDefault("false")
    @Column(name = "isdeleted", nullable = false)
    private Boolean isdeleted = false;

}