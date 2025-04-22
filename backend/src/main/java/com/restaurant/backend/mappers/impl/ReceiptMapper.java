package com.restaurant.backend.mappers.impl;

import com.restaurant.backend.domains.dto.Receipt.ReceiptDto;
import com.restaurant.backend.domains.dto.Receipt.dto.CreateReceiptDto;
import com.restaurant.backend.domains.entities.Receipt;
import com.restaurant.backend.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ReceiptMapper implements Mapper<Receipt, ReceiptDto> {
    private ModelMapper modelMapper = new ModelMapper();
    public ReceiptMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }


    @Override
    public ReceiptDto mapFrom(Receipt receipt) {
        return modelMapper.map(receipt, ReceiptDto.class);
    }

    @Override
    public Receipt mapTo(ReceiptDto receiptDto) {
        return modelMapper.map(receiptDto, Receipt.class);
    }

    public Receipt mapTo(CreateReceiptDto createReceiptDto) {
        Receipt receipt = new Receipt();
        receipt.setRecTime(createReceiptDto.getRecTime());
        receipt.setIsdeleted(createReceiptDto.getIsdeleted() != null ? createReceiptDto.getIsdeleted() : false);
        receipt.setRecPay(BigDecimal.ZERO); // mặc định recPay là 0
        // Các khóa phụ (emp, cus, tab) sẽ được xử lý ở Controller sau khi save, nên bỏ qua
        return receipt;
    }
}
