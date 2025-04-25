package com.restaurant.backend.controllers;

import com.restaurant.backend.domains.dto.DiningTable.DiningTableDto;
import com.restaurant.backend.domains.dto.DiningTable.dto.CreateDiningTableDto;
import com.restaurant.backend.services.DiningTableService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DiningTableController {
    private final DiningTableService diningTableService;

    public DiningTableController(DiningTableService diningTableService) {
        this.diningTableService = diningTableService;
    }

    @PostMapping(path="/tables")
    public ResponseEntity<DiningTableDto> addTable (@RequestBody CreateDiningTableDto createDiningTableDto) {
        DiningTableDto saved = this.diningTableService.createTable(createDiningTableDto);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping(path="/tables/{id}")
    public ResponseEntity<DiningTableDto> getTable(@PathVariable int id){
        DiningTableDto table = this.diningTableService.getTableById(id);
        return table != null ? new ResponseEntity<>(table, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(path="/tables")
    public ResponseEntity<List<DiningTableDto>> getAllTables(){
        return new ResponseEntity<>(this.diningTableService.getAllTables(), HttpStatus.OK);
    }

    @PutMapping(path="/tables/{id}")
    public ResponseEntity<DiningTableDto> updateTable(@PathVariable int id, @RequestBody CreateDiningTableDto createDiningTableDto) {
        DiningTableDto updated = this.diningTableService.updateTable(id, createDiningTableDto);
        return updated != null ? new ResponseEntity<>(updated, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PatchMapping(path="/tables/{id}")
    public ResponseEntity<DiningTableDto> partialUpdateTable(@PathVariable int id, @RequestBody CreateDiningTableDto createDiningTableDto) {
        DiningTableDto updated = this.diningTableService.partialUpdateTable(id, createDiningTableDto);
        return updated != null ? new ResponseEntity<>(updated, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(path="/tables/{id}")
    public ResponseEntity<Boolean> deleteTable(@PathVariable int id){
        boolean deleted = this.diningTableService.softDeleteTable(id);
        return new ResponseEntity<>(deleted, deleted ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }
}