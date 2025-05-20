package com.restaurant.backend.repositories;

import com.restaurant.backend.domains.entities.FinancialHistory;
import com.restaurant.backend.domains.entities.FHType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface FinancialHistoryRepository extends JpaRepository<FinancialHistory, Long> {
    List<FinancialHistory> findByType(FHType type);

    List<FinancialHistory> findByFinDateBetween(Instant startDate, Instant endDate);

    List<FinancialHistory> findByReferenceTypeAndReferenceId(String referenceType, Integer referenceId);
}
