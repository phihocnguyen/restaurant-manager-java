package com.restaurant.backend.mappers.impl;

import com.restaurant.backend.domains.dto.Receipt.ReceiptDto;
import com.restaurant.backend.domains.dto.Receipt.dto.CreateReceiptDto;
import com.restaurant.backend.domains.entities.Receipt;
import com.restaurant.backend.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

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

    public Receipt mapTo(CreateReceiptDto createReceiptDto) {return modelMapper.map(createReceiptDto, Receipt.class);}
}
