package com.restaurant.backend.controllers;

import com.restaurant.backend.domains.dto.Stockin.StockinDto;
import com.restaurant.backend.services.StockinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stock-in")
public class StockinController {
    private final StockinService stockinService;

    @Autowired
    public StockinController(StockinService stockinService) {
        this.stockinService = stockinService;
    }

    @PostMapping
    public ResponseEntity<StockinDto> createStockIn(@RequestBody StockinDto stockinDto) {
        StockinDto createdStockin = stockinService.createStockIn(stockinDto);
        return new ResponseEntity<>(createdStockin, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<StockinDto>> getAllStockIns() {
        List<StockinDto> stockins = stockinService.getAllStockIns();
        return new ResponseEntity<>(stockins, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockinDto> getStockInById(@PathVariable Integer id) {
        try {
            StockinDto stockin = stockinService.getStockInById(id);
            return new ResponseEntity<>(stockin, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<StockinDto> updateStockIn(@PathVariable Integer id, @RequestBody StockinDto stockinDto) {
        try {
            StockinDto updatedStockin = stockinService.updateStockIn(id, stockinDto);
            return new ResponseEntity<>(updatedStockin, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStockIn(@PathVariable Integer id) {
        try {
            stockinService.deleteStockIn(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}