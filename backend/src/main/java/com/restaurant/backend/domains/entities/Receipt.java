package com.restaurant.backend.domains.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "receipts")
public class Receipt {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "receipts_id_gen")
    @SequenceGenerator(name = "receipts_id_gen", sequenceName = "receipts_rec_id_seq", allocationSize = 1)
    @Column(name = "rec_id", nullable = false)
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

    @OneToMany(mappedBy = "rec", cascade = CascadeType.ALL)
    private List<ReceiptDetail> recDetails;

    @NotNull
    @Column(name = "rec_time", nullable = true)
    private Instant recTime;

    @NotNull
    @ColumnDefault("0.00")
    @Column(name = "rec_pay", nullable = false, precision = 18, scale = 2)
    private BigDecimal recPay;

    @NotNull
    @ColumnDefault("false")
    @Column(name = "isdeleted", nullable = false)
    private Boolean isdeleted = false;

    @Column(name = "payment_method", nullable = true)
    private String paymentMethod;

    // run right before that instance added to the db
    @PrePersist
    public void prePersist(){
        if(recTime == null){
            recTime = Instant.now();
        }
    }

}