package com.restaurant.backend.controllers;

import com.restaurant.backend.domains.dto.DiningTable.DiningTableDto;
import com.restaurant.backend.domains.dto.DiningTable.dto.CreateDiningTableDto;
import com.restaurant.backend.domains.entities.DiningTable;
import com.restaurant.backend.mappers.impl.DiningTableMapper;
import com.restaurant.backend.services.DiningTableService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class DiningTableController {
    private final DiningTableService diningTableService;
    private final DiningTableMapper diningTableMapper;
    public DiningTableController(DiningTableService diningTableService, DiningTableMapper diningTableMapper) {
        this.diningTableService = diningTableService;
        this.diningTableMapper = diningTableMapper;
    }

    @PostMapping(path="/tables")
    public ResponseEntity<DiningTableDto> addTable (@RequestBody CreateDiningTableDto createDiningTableDto) {
        DiningTable diningTable = this.diningTableMapper.mapTo(createDiningTableDto);
        DiningTable savedDiningTable = this.diningTableService.save(diningTable);
        return new ResponseEntity<>(this.diningTableMapper.mapFrom(savedDiningTable), HttpStatus.CREATED);
    } // might not ever use it

    @GetMapping(path="/tables/{id}")
    public ResponseEntity<DiningTableDto> getTable(@PathVariable int id){
        DiningTable dbDiningTable = this.diningTableService.findOneById(id);
        if(dbDiningTable == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(this.diningTableMapper.mapFrom(dbDiningTable), HttpStatus.OK);
    }

    @GetMapping(path="/tables")
    public ResponseEntity<List<DiningTableDto>> getAllTables(){
        //List<DiningTable> allTables = this.diningTableService.findAll();
        return new ResponseEntity<>(this.diningTableService.findAll().stream().map(this.diningTableMapper::mapFrom).collect(Collectors.toList()), HttpStatus.OK);
    }

    @PutMapping(path="/tables/{id}")
    public ResponseEntity<DiningTableDto> updateTable(@PathVariable int id, @RequestBody CreateDiningTableDto createDiningTableDto) {
        DiningTable dbDiningTable = this.diningTableService.findOneById(id);
        if(dbDiningTable == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        DiningTable updatedDiningTable = this.diningTableMapper.mapTo(createDiningTableDto);
        updatedDiningTable.setId(id);
        DiningTable savedDiningTable = this.diningTableService.save(updatedDiningTable);
        return new ResponseEntity<>(this.diningTableMapper.mapFrom(savedDiningTable), HttpStatus.OK);
    }

    @PatchMapping(path="/tables/{id}")
    public ResponseEntity<DiningTableDto> partialUpdateTable(@PathVariable int id, @RequestBody CreateDiningTableDto createDiningTableDto) {
        DiningTable dbDiningTable = this.diningTableService.findOneById(id);
        if (dbDiningTable == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        //dbDiningTable.setId(id);
        if(createDiningTableDto.getTabNum() != null) {
            dbDiningTable.setTabNum(createDiningTableDto.getTabNum());
        }
        if(createDiningTableDto.getTabStatus() != null) {
            dbDiningTable.setTabStatus(createDiningTableDto.getTabStatus());
        }
        if(createDiningTableDto.getIsdeleted() != null){
            dbDiningTable.setIsdeleted(createDiningTableDto.getIsdeleted());
        }
        DiningTable savedDiningTable = this.diningTableService.save(dbDiningTable);
        return new ResponseEntity<>(this.diningTableMapper.mapFrom(savedDiningTable), HttpStatus.OK);
    }
    @DeleteMapping(path="/tables/{id}")
    public ResponseEntity<Boolean> deleteTable(@PathVariable int id){
        DiningTable dbDiningTable = this.diningTableService.findOneById(id);
        if(dbDiningTable == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        dbDiningTable.setIsdeleted(true);
        this.diningTableService.save(dbDiningTable);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}
