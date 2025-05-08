package com.restaurant.backend.services;

import com.restaurant.backend.domains.dto.Receipt.ReceiptDto;
import com.restaurant.backend.domains.dto.Receipt.dto.CreateReceiptDto;
import com.restaurant.backend.domains.entities.Receipt;

import java.util.List;
import java.util.Optional;

public interface ReceiptService {
    public Receipt save(Receipt receipt);
    public List<Receipt> findAll();
    public Optional<Receipt> findById(int id);
    public ReceiptDto create(CreateReceiptDto dto);
    public ReceiptDto update(int id, CreateReceiptDto dto);
    public ReceiptDto partialUpdate(int id, CreateReceiptDto dto);
    public boolean softDelete(int id);
    public List<ReceiptDto> getAllReceipts();
    public ReceiptDto getReceiptById(int id);
}
