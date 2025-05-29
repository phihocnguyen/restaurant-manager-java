package com.restaurant.backend.services.impl;

import com.restaurant.backend.domains.dto.Voucher.VoucherDto;
import com.restaurant.backend.domains.dto.Voucher.dto.CreateVoucherDto;
import com.restaurant.backend.domains.entities.Voucher;
import com.restaurant.backend.mappers.VoucherMapper;
import com.restaurant.backend.repositories.VoucherRepository;
import com.restaurant.backend.services.VoucherService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VoucherServiceImpl implements VoucherService {
    private final VoucherRepository voucherRepository;
    private final VoucherMapper voucherMapper;

    public VoucherServiceImpl(VoucherRepository voucherRepository, VoucherMapper voucherMapper) {
        this.voucherRepository = voucherRepository;
        this.voucherMapper = voucherMapper;
    }

    @Override
    public VoucherDto createVoucher(CreateVoucherDto dto) {
        Voucher voucher = voucherMapper.mapTo(dto);
        return voucherMapper.mapFrom(voucherRepository.save(voucher));
    }

    @Override
    public Optional<VoucherDto> getVoucherById(Integer id) {
        return voucherRepository.findById(id).map(voucherMapper::mapFrom);
    }

    @Override
    public List<VoucherDto> getAllVouchers() {
        return voucherRepository.findAll().stream()
                .map(voucherMapper::mapFrom)
                .collect(Collectors.toList());
    }

    @Override
    public VoucherDto updateVoucher(Integer id, CreateVoucherDto dto) {
        Optional<Voucher> existingVoucher = voucherRepository.findById(id);
        if (existingVoucher.isPresent()) {
            Voucher updatedVoucher = voucherMapper.mapTo(dto);
            updatedVoucher.setId(id);
            return voucherMapper.mapFrom(voucherRepository.save(updatedVoucher));
        }
        return null; // Or throw an exception
    }

    @Override
    public boolean deleteVoucher(Integer id) {
        if (voucherRepository.existsById(id)) {
            voucherRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // TODO: Implement method for redeeming points for voucher
} 