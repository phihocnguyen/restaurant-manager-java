package com.restaurant.backend.controllers;

import com.restaurant.backend.services.StockinDetailsDrinkOtherService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StockinDetailsDrinkOtherController {
    private final StockinDetailsDrinkOtherService stockinDetailsDrinkOtherService;
    public StockinDetailsDrinkOtherController(StockinDetailsDrinkOtherService stockinDetailsDrinkOtherService) {
        this.stockinDetailsDrinkOtherService = stockinDetailsDrinkOtherService;
    }
}
