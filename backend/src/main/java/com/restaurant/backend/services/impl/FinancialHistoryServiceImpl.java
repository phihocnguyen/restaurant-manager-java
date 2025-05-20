package com.restaurant.backend.services.impl;

import com.restaurant.backend.domains.entities.FinancialHistory;
import com.restaurant.backend.domains.entities.FHType;
import com.restaurant.backend.repositories.FinancialHistoryRepository;
import com.restaurant.backend.services.FinancialHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
public class FinancialHistoryServiceImpl implements FinancialHistoryService {
    private final FinancialHistoryRepository financialHistoryRepository;

    @Autowired
    public FinancialHistoryServiceImpl(FinancialHistoryRepository financialHistoryRepository) {
        this.financialHistoryRepository = financialHistoryRepository;
    }

    @Override
    public List<FinancialHistory> getAllHistory() {
        return financialHistoryRepository.findAll();
    }

    @Override
    public FinancialHistory getHistoryById(Long id) {
        return financialHistoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Financial history not found"));
    }

    @Override
    public List<FinancialHistory> getHistoryByType(String type) {
        try {
            FHType historyType = FHType.valueOf(type.toUpperCase());
            return financialHistoryRepository.findByType(historyType);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid financial history type");
        }
    }

    @Override
    public List<FinancialHistory> getHistoryByDateRange(Instant startDate, Instant endDate) {
        return financialHistoryRepository.findByFinDateBetween(startDate, endDate);
    }

    @Override
    public List<FinancialHistory> getHistoryByReference(String referenceType, Integer referenceId) {
        return financialHistoryRepository.findByReferenceTypeAndReferenceId(referenceType, referenceId);
    }

    @Override
    @Transactional
    public void deleteHistory(Long id) {
        FinancialHistory history = getHistoryById(id);
        financialHistoryRepository.delete(history);
    }

    @Override
    @Transactional
    public FinancialHistory createStockInHistory(Long stockInId, Double price) {
        FinancialHistory financialHistory = new FinancialHistory();
        financialHistory.setFinDate(Instant.now());
        financialHistory.setDescription("Stock-in record #" + stockInId);
        financialHistory.setType(FHType.EXPENSE);
        financialHistory.setAmount(BigDecimal.valueOf(price));
        financialHistory.setReferenceId(stockInId.intValue());
        financialHistory.setReferenceType("STOCKIN");
        return financialHistoryRepository.save(financialHistory);
    }
}
