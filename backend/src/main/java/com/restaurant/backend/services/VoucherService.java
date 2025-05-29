package com.restaurant.backend.services;

import com.restaurant.backend.domains.dto.Voucher.VoucherDto;
import com.restaurant.backend.domains.dto.Voucher.dto.CreateVoucherDto;

import java.util.List;
import java.util.Optional;

public interface VoucherService {
    VoucherDto createVoucher(CreateVoucherDto dto);
    Optional<VoucherDto> getVoucherById(Integer id);
    List<VoucherDto> getAllVouchers();
    VoucherDto updateVoucher(Integer id, CreateVoucherDto dto);
    boolean deleteVoucher(Integer id);
    // TODO: Add method for redeeming points for voucher
} 