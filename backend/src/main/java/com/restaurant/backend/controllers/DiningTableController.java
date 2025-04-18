package com.restaurant.backend.controllers;

import com.restaurant.backend.services.DiningTableService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DiningTableController {
    private final DiningTableService diningTableService;
    public DiningTableController(DiningTableService diningTableService) {
        this.diningTableService = diningTableService;
    }
}
