package com.restaurant.backend.domains.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "account")
public class Account {
    @Id
    @Size(max = 100)
    @SequenceGenerator(name = "account_id_gen", sequenceName = "account_role_role_id_seq", allocationSize = 1)
    @Column(name = "acc_username", nullable = false, length = 100)
    private String accUsername;

    @Size(max = 100)
    @NotNull
    @Column(name = "acc_password", nullable = false, length = 100)
    private String accPassword;

    @Size(max = 100)
    @Column(name = "acc_email", length = 100, nullable = false)
    private String accEmail;

    @Size(max = 100)
    @NotNull
    @Column(name = "acc_displayname", nullable = false, length = 100)
    private String accDisplayname;

    @Size(max = 5)
    @NotNull
    @Column(name = "acc_gender", nullable = false, length = 5)
    private String accGender;

    @NotNull
    @Column(name = "acc_bday", nullable = false)
    private LocalDate accBday;

    @Size(max = 100)
    @NotNull
    @Column(name = "acc_address", nullable = false, length = 100)
    private String accAddress;

    @Size(max = 20)
    @NotNull
    @Column(name = "acc_phone", nullable = false, length = 20)
    private String accPhone;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "role_id")
    private AccountRole role;

    @NotNull
    @ColumnDefault("false")
    @Column(name = "isdeleted", nullable = false)
    private Boolean isdeleted = false;

}