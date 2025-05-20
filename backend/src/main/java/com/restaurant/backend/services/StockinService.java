package com.restaurant.backend.services;

import com.restaurant.backend.domains.dto.Stockin.StockinDto;
import java.util.List;

public interface StockinService {
    StockinDto createStockIn(StockinDto stockinDto);

    StockinDto updateStockIn(Integer id, StockinDto stockinDto);

    void deleteStockIn(Integer id);

    List<StockinDto> getAllStockIns();

    StockinDto getStockInById(Integer id);
}