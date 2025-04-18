package com.restaurant.backend.services.impl;

import com.restaurant.backend.repositories.StockinDetailsIngreRepository;
import com.restaurant.backend.services.StockinDetailsIngreService;
import org.springframework.stereotype.Service;

@Service
public class StockinDetailsIngreServiceImpl implements StockinDetailsIngreService {
    private final StockinDetailsIngreRepository stockinDetailsIngreRepository;
    public StockinDetailsIngreServiceImpl(StockinDetailsIngreRepository stockinDetailsIngreRepository) {
        this.stockinDetailsIngreRepository = stockinDetailsIngreRepository;
    }
}
