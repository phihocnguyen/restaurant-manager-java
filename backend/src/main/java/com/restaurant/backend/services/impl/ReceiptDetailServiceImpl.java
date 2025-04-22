package com.restaurant.backend.services.impl;

import com.restaurant.backend.domains.entities.ReceiptDetail;
import com.restaurant.backend.domains.entities.ReceiptDetailId;
import com.restaurant.backend.repositories.ReceiptDetailRepository;
import com.restaurant.backend.services.ReceiptDetailService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReceiptDetailServiceImpl implements ReceiptDetailService {
    private final ReceiptDetailRepository receiptDetailRepository;
    public ReceiptDetailServiceImpl(ReceiptDetailRepository receiptDetailRepository) {
        this.receiptDetailRepository = receiptDetailRepository;
    }

    public ReceiptDetail save(ReceiptDetail receiptDetail) {
        return receiptDetailRepository.save(receiptDetail);
    }

    public List<ReceiptDetail> saveAll(List<ReceiptDetail> receiptDetails) {
        return receiptDetailRepository.saveAll(receiptDetails);
    }

    public List<ReceiptDetail> findAllByRecId(Integer recId) {
        return receiptDetailRepository.findAllByRecId(recId);
    }

    public void deleteAll(List<ReceiptDetail> receiptDetails) {
        receiptDetailRepository.deleteAll(receiptDetails);
    }

    public Optional<ReceiptDetail> findById(ReceiptDetailId receiptDetailId) {
        return receiptDetailRepository.findById(receiptDetailId);
    }

    public void deleteById(ReceiptDetailId receiptDetailId) {
        receiptDetailRepository.deleteById(receiptDetailId);
    }
}
