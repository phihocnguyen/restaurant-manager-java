package com.restaurant.backend.mappers.impl;

import com.restaurant.backend.domains.dto.FinancialHistory.FinancialHistoryDto;
import com.restaurant.backend.domains.entities.FinancialHistory;
import com.restaurant.backend.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class FinancialHistoryMapper implements Mapper<FinancialHistory, FinancialHistoryDto> {
    private ModelMapper modelMapper = new ModelMapper();
    public FinancialHistoryMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public FinancialHistoryDto mapFrom(FinancialHistory financialHistory) {
        return null;
    }

    @Override
    public FinancialHistory mapTo(FinancialHistoryDto financialHistoryDto) {
        return null;
    }
}
