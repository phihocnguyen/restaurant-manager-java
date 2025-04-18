package com.restaurant.backend.services.impl;

import com.restaurant.backend.repositories.FinancialHistoryRepository;
import com.restaurant.backend.repositories.ReceiptRepository;
import com.restaurant.backend.services.ReceiptService;
import org.springframework.stereotype.Service;

@Service
public class ReceiptServiceImpl implements ReceiptService {
    private final ReceiptRepository receiptRepository;
    public ReceiptServiceImpl(ReceiptRepository receiptRepository) {
        this.receiptRepository = receiptRepository;
    }
}
