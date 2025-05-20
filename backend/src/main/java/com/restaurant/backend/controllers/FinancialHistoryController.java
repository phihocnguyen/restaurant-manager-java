package com.restaurant.backend.controllers;

import com.restaurant.backend.domains.entities.FinancialHistory;
import com.restaurant.backend.services.FinancialHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/financial-history")
public class FinancialHistoryController {

    @Autowired
    private FinancialHistoryService financialHistoryService;

    @GetMapping
    public ResponseEntity<List<FinancialHistory>> getAllHistory() {
        return ResponseEntity.ok(financialHistoryService.getAllHistory());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FinancialHistory> getHistoryById(@PathVariable Long id) {
        return ResponseEntity.ok(financialHistoryService.getHistoryById(id));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<FinancialHistory>> getHistoryByType(@PathVariable String type) {
        return ResponseEntity.ok(financialHistoryService.getHistoryByType(type));
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<FinancialHistory>> getHistoryByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant endDate) {
        return ResponseEntity.ok(financialHistoryService.getHistoryByDateRange(startDate, endDate));
    }

    @GetMapping("/reference/{referenceType}/{referenceId}")
    public ResponseEntity<List<FinancialHistory>> getHistoryByReference(
            @PathVariable String referenceType,
            @PathVariable Integer referenceId) {
        return ResponseEntity.ok(financialHistoryService.getHistoryByReference(referenceType, referenceId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHistory(@PathVariable Long id) {
        financialHistoryService.deleteHistory(id);
        return ResponseEntity.ok().build();
    }
}
