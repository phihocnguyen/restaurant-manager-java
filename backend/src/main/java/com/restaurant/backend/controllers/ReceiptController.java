package com.restaurant.backend.controllers;

import com.restaurant.backend.domains.dto.Receipt.ReceiptDto;
import com.restaurant.backend.domains.dto.Receipt.dto.CreateReceiptDto;
import com.restaurant.backend.domains.entities.Customer;
import com.restaurant.backend.domains.entities.DiningTable;
import com.restaurant.backend.domains.entities.Employee;
import com.restaurant.backend.domains.entities.Receipt;
import com.restaurant.backend.mappers.impl.ReceiptMapper;
import com.restaurant.backend.services.CustomerService;
import com.restaurant.backend.services.DiningTableService;
import com.restaurant.backend.services.EmployeeService;
import com.restaurant.backend.services.ReceiptService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class ReceiptController {
    private final ReceiptService receiptService;
    private final ReceiptMapper receiptMapper;
    private final EmployeeService employeeService;
    private final CustomerService customerService;
    private final DiningTableService diningTableService;
    public ReceiptController(ReceiptService receiptService, ReceiptMapper receiptMapper, EmployeeService employeeService, CustomerService customerService, DiningTableService diningTableService) {
        this.receiptService = receiptService;
        this.receiptMapper = receiptMapper;
        this.employeeService = employeeService;
        this.customerService = customerService;
        this.diningTableService = diningTableService;
    }

    @PostMapping(path="/receipts")
    public ResponseEntity<ReceiptDto> addReceipt(@RequestBody CreateReceiptDto createReceiptDto) {
        Receipt receipt = this.receiptMapper.mapTo(createReceiptDto);
        if(createReceiptDto.getEmpId() != null){
            Optional<Employee> foundEmp = this.employeeService.findById(createReceiptDto.getEmpId());
            if(!foundEmp.isPresent()){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            receipt.setEmp(foundEmp.get());
        }
        if(createReceiptDto.getIsdeleted() != null){
            receipt.setIsdeleted(createReceiptDto.getIsdeleted());
        }
        if(createReceiptDto.getCusId() != null){
            Optional<Customer> foundCus = this.customerService.findById(createReceiptDto.getCusId());
            if(!foundCus.isPresent()){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            receipt.setCus(foundCus.get());
        }
        if(createReceiptDto.getTabId() != null){
            Optional<DiningTable> foundTab = this.diningTableService.findById(createReceiptDto.getTabId());
            if(!foundTab.isPresent()){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            receipt.setTab(foundTab.get());
        }
        if(createReceiptDto.getRecPay() != null){
            receipt.setRecPay(createReceiptDto.getRecPay());
        }
        if(createReceiptDto.getRecTime() != null){
            receipt.setRecTime(createReceiptDto.getRecTime());
        }
        Receipt savedReceipt = this.receiptService.save(receipt);
        return new ResponseEntity<>(this.receiptMapper.mapFrom(savedReceipt), HttpStatus.CREATED);
    }

    @GetMapping(path="/receipts")
    public ResponseEntity<List<ReceiptDto>> getAllReceipts() {
        return new ResponseEntity<>(this.receiptService.findAll().stream().map(this.receiptMapper::mapFrom).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping(path="/receipts/{recId}")
    public ResponseEntity<ReceiptDto> getReceipt(@PathVariable int recId) {
        Optional<Receipt> dbReceipt = this.receiptService.findById(recId);
        if(!dbReceipt.isPresent() || !dbReceipt.get().getIsdeleted()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Receipt receipt = dbReceipt.get();
        return new ResponseEntity<>(this.receiptMapper.mapFrom(receipt), HttpStatus.OK);
    }

    @PutMapping(path="/receipts/{recId}")
    public ResponseEntity<ReceiptDto> updateReceipt(@PathVariable int recId, @RequestBody CreateReceiptDto createReceiptDto) {
        Optional<Receipt> dbReceipt = this.receiptService.findById(recId);
        if(!dbReceipt.isPresent() || dbReceipt.get().getIsdeleted()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Receipt receipt = this.receiptMapper.mapTo(createReceiptDto);
        receipt.setId(recId);
        Receipt savedReceipt = this.receiptService.save(receipt);
        return new ResponseEntity<>(this.receiptMapper.mapFrom(savedReceipt), HttpStatus.OK);
    }

    @PatchMapping(path="/receipts/{recId}")
    public ResponseEntity<ReceiptDto> partialUpdateReceipt(@PathVariable int recId, @RequestBody CreateReceiptDto createReceiptDto) {
        Optional<Receipt> dbReceipt = this.receiptService.findById(recId);
        if(!dbReceipt.isPresent() || dbReceipt.get().getIsdeleted()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(createReceiptDto.getEmpId() != null){
            Optional<Employee> foundEmp = this.employeeService.findById(createReceiptDto.getEmpId());
            if(!foundEmp.isPresent()){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            dbReceipt.get().setEmp(foundEmp.get());
        }
        if(createReceiptDto.getIsdeleted() != null){
            dbReceipt.get().setIsdeleted(createReceiptDto.getIsdeleted());
        }
        if(createReceiptDto.getCusId() != null){
            Optional<Customer> foundCus = this.customerService.findById(createReceiptDto.getCusId());
            if(!foundCus.isPresent()){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            dbReceipt.get().setCus(foundCus.get());
        }
        if(createReceiptDto.getTabId() != null){
            Optional<DiningTable> foundTab = this.diningTableService.findById(createReceiptDto.getTabId());
            if(!foundTab.isPresent()){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            dbReceipt.get().setTab(foundTab.get());
        }
        if(createReceiptDto.getRecPay() != null){
            dbReceipt.get().setRecPay(createReceiptDto.getRecPay());
        }
        if(createReceiptDto.getRecTime() != null){
            dbReceipt.get().setRecTime(createReceiptDto.getRecTime());
        }
        Receipt savedReceipt = this.receiptService.save(dbReceipt.get());
        return new ResponseEntity<>(this.receiptMapper.mapFrom(savedReceipt), HttpStatus.OK);
    }

    @DeleteMapping(path="/receipts/{recId}")
    public ResponseEntity<Boolean> deleteReceipt(@PathVariable Integer recId){
        Optional<Receipt> dbReceipt = this.receiptService.findById(recId);
        if(!dbReceipt.isPresent() || dbReceipt.get().getIsdeleted()) {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
        dbReceipt.get().setIsdeleted(true);
        Receipt savedReceipt = this.receiptService.save(dbReceipt.get());
        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}
