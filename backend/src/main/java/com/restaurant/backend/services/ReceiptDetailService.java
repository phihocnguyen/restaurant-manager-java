package com.restaurant.backend.services;

import com.restaurant.backend.domains.entities.ReceiptDetail;
import com.restaurant.backend.domains.entities.ReceiptDetailId;

import java.util.List;
import java.util.Optional;

public interface ReceiptDetailService {
    public ReceiptDetail save(ReceiptDetail receiptDetail);
    public List<ReceiptDetail> saveAll(List<ReceiptDetail> receiptDetails);
    public List<ReceiptDetail> findAllByRecId(Integer recId);
    public void deleteAll(List<ReceiptDetail> receiptDetails);
    public Optional<ReceiptDetail> findById(ReceiptDetailId receiptDetailId);
    public void deleteById(ReceiptDetailId receiptDetailId);
}
