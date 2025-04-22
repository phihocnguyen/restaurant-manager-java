package com.restaurant.backend.mappers.impl;

import com.restaurant.backend.domains.dto.ReceiptDetail.ReceiptDetailDto;
import com.restaurant.backend.domains.dto.ReceiptDetail.dto.CreateReceiptDetailDto;
import com.restaurant.backend.domains.entities.ReceiptDetail;
import com.restaurant.backend.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ReceiptDetailMapper implements Mapper<ReceiptDetail, ReceiptDetailDto> {
    private ModelMapper modelMapper = new ModelMapper();
    public ReceiptDetailMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
    @Override
    public ReceiptDetailDto mapFrom(ReceiptDetail receiptDetail) {
        return modelMapper.map(receiptDetail, ReceiptDetailDto.class);
    }

    @Override
    public ReceiptDetail mapTo(ReceiptDetailDto receiptDetailDto) {
        return modelMapper.map(receiptDetailDto, ReceiptDetail.class);
    }

    public ReceiptDetail mapTo(CreateReceiptDetailDto createReceiptDetailDto) {
        return ReceiptDetail.builder()
                .id(null)
                .quantity(createReceiptDetailDto.getQuantity())
                .build();
    }
}
