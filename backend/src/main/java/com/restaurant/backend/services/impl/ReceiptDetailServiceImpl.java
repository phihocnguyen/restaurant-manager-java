package com.restaurant.backend.services.impl;

import com.restaurant.backend.repositories.ReceiptDetailRepository;
import com.restaurant.backend.services.ReceiptDetailService;
import org.springframework.stereotype.Service;

@Service
public class ReceiptDetailServiceImpl implements ReceiptDetailService {
    private final ReceiptDetailRepository receiptDetailRepository;
    public ReceiptDetailServiceImpl(ReceiptDetailRepository receiptDetailRepository) {
        this.receiptDetailRepository = receiptDetailRepository;
    }
}
