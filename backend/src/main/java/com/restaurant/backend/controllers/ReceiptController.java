package com.restaurant.backend.controllers;

import com.restaurant.backend.domains.dto.Receipt.ReceiptDto;
import com.restaurant.backend.domains.dto.Receipt.dto.CreateReceiptDto;
import com.restaurant.backend.domains.dto.Receipt.dto.ReceiptHistoryDto;
import com.restaurant.backend.services.ReceiptService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ReceiptController {
    private final ReceiptService receiptService;

    public ReceiptController(ReceiptService receiptService) {
        this.receiptService = receiptService;
    }

    @PostMapping("/receipts")
    public ResponseEntity<ReceiptDto> addReceipt(@RequestBody CreateReceiptDto dto) {
        ReceiptDto created = receiptService.create(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/receipts")
    public ResponseEntity<List<ReceiptHistoryDto>> getAllReceipts() {
        List<ReceiptHistoryDto> receipts = receiptService.getAllReceipts();
        return new ResponseEntity<>(receipts, HttpStatus.OK);
    }

    @GetMapping("/receipts/{recId}")
    public ResponseEntity<ReceiptDto> getReceipt(@PathVariable int recId) {
        var receipt = receiptService.getReceiptById(recId);
        if (receipt == null || Boolean.TRUE.equals(receipt.getIsdeleted())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(receipt, HttpStatus.OK);
    }

    @PutMapping("/receipts/{recId}")
    public ResponseEntity<ReceiptDto> updateReceipt(@PathVariable int recId, @RequestBody CreateReceiptDto dto) {
        return new ResponseEntity<>(receiptService.update(recId, dto), HttpStatus.OK);
    }

    @PatchMapping("/receipts/{recId}")
    public ResponseEntity<ReceiptDto> partialUpdateReceipt(@PathVariable int recId, @RequestBody CreateReceiptDto dto) {
        return new ResponseEntity<>(receiptService.partialUpdate(recId, dto), HttpStatus.OK);
    }

    @DeleteMapping("/receipts/{recId}")
    public ResponseEntity<Boolean> deleteReceipt(@PathVariable int recId) {
        boolean deleted = receiptService.softDelete(recId);
        return new ResponseEntity<>(deleted, deleted ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }
}