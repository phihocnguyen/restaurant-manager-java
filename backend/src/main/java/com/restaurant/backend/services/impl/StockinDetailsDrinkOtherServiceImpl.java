package com.restaurant.backend.services.impl;

import com.restaurant.backend.repositories.StockinDetailsDrinkOtherRepository;
import com.restaurant.backend.services.StockinDetailsDrinkOtherService;
import org.springframework.stereotype.Service;

@Service
public class StockinDetailsDrinkOtherServiceImpl implements StockinDetailsDrinkOtherService {
    private final StockinDetailsDrinkOtherRepository stockinDetailsDrinkOtherRepository;
    public StockinDetailsDrinkOtherServiceImpl(StockinDetailsDrinkOtherRepository stockinDetailsDrinkOtherRepository) {
        this.stockinDetailsDrinkOtherRepository = stockinDetailsDrinkOtherRepository;
    }
}
