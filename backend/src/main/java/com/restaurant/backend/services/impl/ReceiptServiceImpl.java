package com.restaurant.backend.services.impl;

import com.restaurant.backend.domains.entities.Receipt;
import com.restaurant.backend.repositories.FinancialHistoryRepository;
import com.restaurant.backend.repositories.ReceiptRepository;
import com.restaurant.backend.services.ReceiptService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReceiptServiceImpl implements ReceiptService {
    private final ReceiptRepository receiptRepository;
    public ReceiptServiceImpl(ReceiptRepository receiptRepository) {
        this.receiptRepository = receiptRepository;
    }

    public Receipt save(Receipt receipt) {
        return receiptRepository.save(receipt);
    }

    public List<Receipt> findAll() {
        return receiptRepository.findAll().stream().filter(receipt -> receipt.getIsdeleted()).toList();
    }

    public Optional<Receipt> findById(int id) {
        return receiptRepository.findById(id);
    }
}
