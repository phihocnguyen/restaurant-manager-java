package com.restaurant.backend.controllers;

import com.restaurant.backend.services.ReceiptService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReceiptController {
    private final ReceiptService receiptService;
    public ReceiptController(ReceiptService receiptService) {
        this.receiptService = receiptService;
    }
}
