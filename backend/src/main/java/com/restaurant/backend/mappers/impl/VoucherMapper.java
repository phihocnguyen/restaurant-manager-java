package com.restaurant.backend.mappers.impl;

import com.restaurant.backend.domains.dto.Voucher.VoucherDto;
import com.restaurant.backend.domains.dto.Voucher.dto.CreateVoucherDto;
import com.restaurant.backend.domains.entities.Voucher;
import org.springframework.stereotype.Component;

@Component
public class VoucherMapper {
    public Voucher mapTo(CreateVoucherDto dto) {
        Voucher voucher = new Voucher();
        voucher.setName(dto.getName());
        voucher.setDiscountValue(dto.getDiscountValue());
        voucher.setExpiryDate(dto.getExpiryDate());
        return voucher;
    }

    public VoucherDto mapFrom(Voucher voucher) {
        VoucherDto dto = new VoucherDto();
        dto.setId(voucher.getId());
        dto.setName(voucher.getName());
        dto.setDiscountValue(voucher.getDiscountValue());
        dto.setExpiryDate(voucher.getExpiryDate());
        return dto;
    }
} 