package com.restaurant.backend.repositories;

import com.restaurant.backend.domains.entities.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoucherRepository extends JpaRepository<Voucher, Integer> {
} 