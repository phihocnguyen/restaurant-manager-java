package com.restaurant.backend.services;

import com.restaurant.backend.domains.dto.ReceiptDetail.ReceiptDetailDto;
import com.restaurant.backend.domains.dto.ReceiptDetail.dto.CreateManyReceiptDetailsDto;
import com.restaurant.backend.domains.dto.ReceiptDetail.dto.CreateReceiptDetailDto;
import com.restaurant.backend.domains.dto.ReceiptDetail.dto.CreateReceiptWithManyReceiptDetailsDto;
import com.restaurant.backend.domains.dto.ReceiptDetail.dto.CreateReceiptWithOneReceiptDetail;
import com.restaurant.backend.domains.entities.ReceiptDetail;
import com.restaurant.backend.domains.entities.ReceiptDetailId;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface ReceiptDetailService {
    public ReceiptDetail save(ReceiptDetail receiptDetail);
    public ResponseEntity<ReceiptDetailDto> createReceiptWithOneDetail(CreateReceiptWithOneReceiptDetail body);
    public ResponseEntity<ReceiptDetailDto> addDetailToExistingReceipt(Integer recId, CreateReceiptDetailDto dto);
    public ResponseEntity<List<ReceiptDetailDto>> addDetailsToExistingReceipt(Integer recId, CreateManyReceiptDetailsDto dtos);
    public ResponseEntity<List<ReceiptDetailDto>> getAllDetailsForReceipt(Integer recId);
    public ResponseEntity<List<ReceiptDetailDto>> createReceiptWithManyDetails(CreateReceiptWithManyReceiptDetailsDto body);
    public ResponseEntity<List<ReceiptDetailDto>> deleteManyDetails(Integer recId);
    public ResponseEntity<ReceiptDetailDto> deleteOneDetail(Integer recId, Integer itemId);
    public ResponseEntity<ReceiptDetailDto> updateReceiptDetail(Integer recId, Integer itemId, CreateReceiptDetailDto dto);
    public ResponseEntity<List<ReceiptDetailDto>> updateAllReceiptDetails(Integer recId, CreateManyReceiptDetailsDto dtoList);
    //    public List<ReceiptDetail> saveAll(List<ReceiptDetail> receiptDetails);
//    public List<ReceiptDetail> findAllByRecId(Integer recId);
//    public void deleteAll(List<ReceiptDetail> receiptDetails);
    public Optional<ReceiptDetail> findById(ReceiptDetailId receiptDetailId);
//    public void deleteById(ReceiptDetailId receiptDetailId);
}
