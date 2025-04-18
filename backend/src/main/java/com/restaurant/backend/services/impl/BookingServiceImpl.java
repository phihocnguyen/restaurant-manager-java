package com.restaurant.backend.services.impl;

import com.restaurant.backend.repositories.BookingRepository;
import com.restaurant.backend.services.BookingService;
import org.springframework.stereotype.Service;

@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    public BookingServiceImpl(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }
}
