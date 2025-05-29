package com.restaurant.backend.mappers;

import com.restaurant.backend.domains.dto.Voucher.VoucherDto;
import com.restaurant.backend.domains.dto.Voucher.dto.CreateVoucherDto;
import com.restaurant.backend.domains.entities.Voucher;
import org.springframework.stereotype.Component;

@Component
public class VoucherMapper {
    public Voucher mapTo(CreateVoucherDto dto) {
        Voucher voucher = new Voucher();
        voucher.setCode(dto.getCode());
        voucher.setDiscount(dto.getDiscount());
        voucher.setExpiryDate(dto.getExpiryDate());
        return voucher;
    }

    public VoucherDto mapFrom(Voucher voucher) {
        VoucherDto dto = new VoucherDto();
        dto.setId(voucher.getId());
        dto.setCode(voucher.getCode());
        dto.setDiscount(voucher.getDiscount());
        dto.setExpiryDate(voucher.getExpiryDate());
        return dto;
    }
} 