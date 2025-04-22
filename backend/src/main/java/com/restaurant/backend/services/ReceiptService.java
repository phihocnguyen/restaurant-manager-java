package com.restaurant.backend.services;

import com.restaurant.backend.domains.entities.Receipt;

import java.util.List;
import java.util.Optional;

public interface ReceiptService {
    public Receipt save(Receipt receipt);
    public List<Receipt> findAll();
    public Optional<Receipt> findById(int id);
}
