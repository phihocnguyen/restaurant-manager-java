package com.restaurant.backend.repositories;

import com.restaurant.backend.domains.entities.FinancialHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FinancialHistoryRepository extends JpaRepository<FinancialHistory, Integer> {
}
