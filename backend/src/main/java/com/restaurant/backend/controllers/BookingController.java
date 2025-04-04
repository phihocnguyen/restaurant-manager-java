package com.restaurant.backend.controllers;

import com.restaurant.backend.services.BookingService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookingController {
    private BookingService bookingService;
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }
}
