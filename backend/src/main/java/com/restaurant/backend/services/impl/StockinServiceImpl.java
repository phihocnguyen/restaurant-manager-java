package com.restaurant.backend.services.impl;

import com.restaurant.backend.repositories.StockinRepository;
import com.restaurant.backend.services.StockinService;
import org.springframework.stereotype.Service;

@Service
public class StockinServiceImpl implements StockinService {
    private final StockinRepository stockinRepository;
    public StockinServiceImpl(StockinRepository stockinRepository) {
        this.stockinRepository = stockinRepository;
    }
}
