package com.restaurant.backend.services.impl;

import com.restaurant.backend.domains.dto.Receipt.ReceiptDto;
import com.restaurant.backend.domains.dto.Receipt.dto.CreateReceiptDto;
import com.restaurant.backend.domains.entities.*;
import com.restaurant.backend.mappers.impl.DiningTableMapper;
import com.restaurant.backend.mappers.impl.ReceiptMapper;
import com.restaurant.backend.repositories.ReceiptRepository;
import com.restaurant.backend.services.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.restaurant.backend.domains.dto.DiningTable.enums.TableStatus;
import com.restaurant.backend.domains.dto.DiningTable.dto.CreateDiningTableDto;
import com.restaurant.backend.repositories.ReceiptDetailRepository;
import com.restaurant.backend.domains.dto.DiningTable.DiningTableDto;
import com.restaurant.backend.domains.dto.Receipt.dto.ReceiptHistoryDto;

@Service
public class ReceiptServiceImpl implements ReceiptService {
    private final ReceiptRepository receiptRepository;
    private final ReceiptMapper receiptMapper;
    private final EmployeeService employeeService;
    private final CustomerService customerService;
    private final DiningTableService diningTableService;
    private final DiningTableMapper diningTableMapper;
    private final ReceiptDetailService receiptDetailService;
    private final ReceiptDetailRepository receiptDetailRepository;

    public ReceiptServiceImpl(ReceiptRepository receiptRepository, ReceiptMapper receiptMapper,
                              EmployeeService employeeService, CustomerService customerService,
                              DiningTableService diningTableService, DiningTableMapper diningTableMapper,
                              ReceiptDetailService receiptDetailService, ReceiptDetailRepository receiptDetailRepository) {
        this.receiptRepository = receiptRepository;
        this.receiptMapper = receiptMapper;
        this.employeeService = employeeService;
        this.customerService = customerService;
        this.diningTableService = diningTableService;
        this.diningTableMapper = diningTableMapper;
        this.receiptDetailService = receiptDetailService;
        this.receiptDetailRepository = receiptDetailRepository;
    }

    @Override
    public Receipt save(Receipt receipt) {
        return receiptRepository.save(receipt);
    }

    @Override
    public List<Receipt> findAll() {
        return receiptRepository.findAll().stream()
                .filter(receipt -> !Boolean.TRUE.equals(receipt.getIsdeleted()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Receipt> findById(int id) {
        return receiptRepository.findById(id);
    }

    @Override
    public ReceiptDto create(CreateReceiptDto dto) {
        // Check if table is available
        DiningTableDto tableDto = diningTableService.getTableById(dto.getTabId());
        if (tableDto.getTabStatus() == TableStatus.EMPTY) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bàn chưa được phục vụ");
        }
        DiningTable table = diningTableService.findById(dto.getTabId());
        Receipt receipt = receiptMapper.mapTo(dto);
        if (dto.getEmpId() != null) {
            Employee emp = employeeService.findById(dto.getEmpId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
            receipt.setEmp(emp);
        }
        if (dto.getCusId() != null) {
            Customer cus = customerService.findById(dto.getCusId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
            receipt.setCus(cus);
        }
        receipt.setTab(table);
        receipt.setRecTime(dto.getRecTime());
        receipt.setIsdeleted(dto.getIsdeleted() != null ? dto.getIsdeleted() : false);
        // Save receipt first to get ID
        receipt = receiptRepository.save(receipt);
        // Calculate total amount from receipt details
        List<ReceiptDetail> details = receiptDetailRepository.findAllByRecId(receipt.getId());
        java.math.BigDecimal totalAmount = details.stream()
                .map(ReceiptDetail::getPrice)
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
        receipt.setRecPay(totalAmount);
        // Update table status to EMPTY
        CreateDiningTableDto updateDto = CreateDiningTableDto.builder()
            .tabNum(table.getTabNum())
            .tabStatus(TableStatus.EMPTY)
            .isdeleted(table.getIsdeleted())
            .build();
        diningTableService.updateTable(table.getId(), updateDto);
        return receiptMapper.mapFrom(receiptRepository.save(receipt));
    }

    @Override
    public ReceiptDto update(int id, CreateReceiptDto dto) {
        Optional<Receipt> db = findById(id);
        if (db.isEmpty() || Boolean.TRUE.equals(db.get().getIsdeleted())) 
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        Receipt receipt = receiptMapper.mapTo(dto);
        receipt.setId(id);
        receipt.setEmp(employeeService.findById(dto.getEmpId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST)));
        receipt.setCus(customerService.findById(dto.getCusId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST)));
        receipt.setTab(diningTableService.findById(dto.getTabId()));
        // Calculate total amount
        List<ReceiptDetail> details = receiptDetailRepository.findAllByRecId(id);
        java.math.BigDecimal totalAmount = details.stream()
                .map(ReceiptDetail::getPrice)
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
        receipt.setRecPay(totalAmount);
        return receiptMapper.mapFrom(receiptRepository.save(receipt));
    }

    @Override
    public ReceiptDto partialUpdate(int id, CreateReceiptDto dto) {
        Receipt receipt = findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        if (receipt.getIsdeleted()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        if (dto.getEmpId() != null) {
            Employee emp = employeeService.findById(dto.getEmpId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
            receipt.setEmp(emp);
        }
        if (dto.getCusId() != null) {
            Customer cus = customerService.findById(dto.getCusId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
            receipt.setCus(cus);
        }
        if (dto.getTabId() != null) {
            DiningTable tab = diningTableService.findById(dto.getTabId());
            receipt.setTab(tab);
        }
        if (dto.getRecTime() != null) {
            receipt.setRecTime(dto.getRecTime());
        }
        if (dto.getIsdeleted() != null) {
            receipt.setIsdeleted(dto.getIsdeleted());
        }
        // Calculate total amount
        List<ReceiptDetail> details = receiptDetailRepository.findAllByRecId(id);
        java.math.BigDecimal totalAmount = details.stream()
                .map(ReceiptDetail::getPrice)
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
        receipt.setRecPay(totalAmount);
        return receiptMapper.mapFrom(receiptRepository.save(receipt));
    }

    @Override
    public boolean softDelete(int id) {
        Optional<Receipt> db = receiptRepository.findById(id);
        if (db.isEmpty() || Boolean.TRUE.equals(db.get().getIsdeleted())) return false;

        db.get().setIsdeleted(true);
        receiptRepository.save(db.get());
        return true;
    }

    @Override
    public List<ReceiptHistoryDto> getAllReceipts() {
        return receiptRepository.findAll().stream()
                .filter(receipt -> !Boolean.TRUE.equals(receipt.getIsdeleted()))
                .map(receiptMapper::mapToHistoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public ReceiptDto getReceiptById(int id) {
        return this.receiptMapper.mapFrom(receiptRepository.findById(id).orElse(null));
    }
}