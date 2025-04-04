package com.restaurant.backend.services.impl;

import com.restaurant.backend.repositories.FinancialHistoryRepository;
import com.restaurant.backend.services.FinancialHistoryService;
import org.springframework.stereotype.Service;

@Service
public class FinancialHistoryServiceImpl implements FinancialHistoryService {
    private final FinancialHistoryRepository financialHistoryRepository;
    public FinancialHistoryServiceImpl(FinancialHistoryRepository financialHistoryRepository) {
        this.financialHistoryRepository = financialHistoryRepository;
    }
}
