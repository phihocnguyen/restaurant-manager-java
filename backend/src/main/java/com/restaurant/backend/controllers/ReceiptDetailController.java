package com.restaurant.backend.controllers;

import com.restaurant.backend.domains.dto.ReceiptDetail.ReceiptDetailDto;
import com.restaurant.backend.domains.dto.ReceiptDetail.dto.CreateManyReceiptDetailsDto;
import com.restaurant.backend.domains.dto.ReceiptDetail.dto.CreateReceiptDetailDto;
import com.restaurant.backend.domains.dto.ReceiptDetail.dto.CreateReceiptWithManyReceiptDetailsDto;
import com.restaurant.backend.domains.dto.ReceiptDetail.dto.CreateReceiptWithOneReceiptDetail;
import com.restaurant.backend.services.ReceiptDetailService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/receipt-details")
public class ReceiptDetailController {
    private final ReceiptDetailService receiptDetailService;

    public ReceiptDetailController(ReceiptDetailService receiptDetailService) {
        this.receiptDetailService = receiptDetailService;
    }

    @PostMapping("/one")
    public ResponseEntity<ReceiptDetailDto> addOneReceiptDetail(@RequestBody CreateReceiptWithOneReceiptDetail body) {
        return receiptDetailService.createReceiptWithOneDetail(body);
    }

    @PostMapping("/many")
    public ResponseEntity<List<ReceiptDetailDto>> addManyReceiptDetails(@RequestBody CreateReceiptWithManyReceiptDetailsDto body) {
        return receiptDetailService.createReceiptWithManyDetails(body);
    }

    @PostMapping("/one/{recId}")
    public ResponseEntity<ReceiptDetailDto> addOneReceiptDetailToReceipt(@PathVariable Integer recId, @RequestBody CreateReceiptDetailDto dto) {
        return receiptDetailService.addDetailToExistingReceipt(recId, dto);
    }

    @PostMapping("/many/{recId}")
    public ResponseEntity<List<ReceiptDetailDto>> addManyReceiptDetailsToReceipt(@PathVariable Integer recId, @RequestBody CreateManyReceiptDetailsDto dtos) {
        return receiptDetailService.addDetailsToExistingReceipt(recId, dtos);
    }

    @GetMapping("/{recId}")
    public ResponseEntity<List<ReceiptDetailDto>> getAllReceiptDetails(@PathVariable Integer recId) {
        return receiptDetailService.getAllDetailsForReceipt(recId);
    }

    @PutMapping("/{recId}")
    public ResponseEntity<List<ReceiptDetailDto>> updateAllReceiptDetails(@PathVariable Integer recId, @RequestBody CreateManyReceiptDetailsDto dtos) {
        return receiptDetailService.updateAllReceiptDetails(recId, dtos);
    }

    @PatchMapping("/{recId}/{itemId}")
    public ResponseEntity<ReceiptDetailDto> updateOneReceiptDetail(@PathVariable Integer recId, @PathVariable Integer itemId, @RequestBody CreateReceiptDetailDto dto) {
        return receiptDetailService.updateReceiptDetail(recId, itemId, dto);
    }

    @DeleteMapping("/{recId}/{itemId}")
    public ResponseEntity<ReceiptDetailDto> deleteOneReceiptDetail(@PathVariable Integer recId, @PathVariable Integer itemId) {
        return receiptDetailService.deleteOneDetail(recId, itemId);
    }

    @DeleteMapping("/{recId}")
    public ResponseEntity<List<ReceiptDetailDto>> deleteManyReceiptDetails(@PathVariable Integer recId) {
        return receiptDetailService.deleteManyDetails(recId);
    }
}
