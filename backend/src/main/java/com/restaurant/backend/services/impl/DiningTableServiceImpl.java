package com.restaurant.backend.services.impl;

import com.restaurant.backend.repositories.DiningTableRepository;
import com.restaurant.backend.services.DiningTableService;
import org.springframework.stereotype.Service;

@Service
public class DiningTableServiceImpl implements DiningTableService {
    private final DiningTableRepository diningTableRepository;
    public DiningTableServiceImpl(DiningTableRepository diningTableRepository) {
        this.diningTableRepository = diningTableRepository;
    }
}
