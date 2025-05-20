package com.restaurant.backend.services;

import com.restaurant.backend.domains.entities.FinancialHistory;
import com.restaurant.backend.domains.entities.FHType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public interface FinancialHistoryService {
    List<FinancialHistory> getAllHistory();

    FinancialHistory getHistoryById(Long id);

    List<FinancialHistory> getHistoryByType(String type);

    List<FinancialHistory> getHistoryByDateRange(Instant startDate, Instant endDate);

    List<FinancialHistory> getHistoryByReference(String referenceType, Integer referenceId);

    void deleteHistory(Long id);

    FinancialHistory createStockInHistory(Long stockInId, Double price);
}
