package com.restaurant.backend.services;

import com.restaurant.backend.domains.dto.Booking.BookingDto;
import com.restaurant.backend.domains.dto.Booking.dto.CreateBookingDto;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.List;

public interface BookingService {
    ResponseEntity<BookingDto> createBooking(CreateBookingDto dto);

    ResponseEntity<BookingDto> getBookingById(Integer id);

    ResponseEntity<List<BookingDto>> getAllBookings();

    ResponseEntity<List<BookingDto>> getBookingsByDateRange(Instant start, Instant end);

    ResponseEntity<List<BookingDto>> getBookingsByTableAndDateRange(Integer tableId, Instant start,
            Instant end);

    ResponseEntity<List<BookingDto>> getBookingsByStatus(Short status);

    ResponseEntity<List<BookingDto>> getBookingsByCustomer(Integer customerId);

    ResponseEntity<List<BookingDto>> getBookingsByEmployee(Integer employeeId);

    ResponseEntity<BookingDto> updateBookingStatus(Integer id, Short status);

    ResponseEntity<Boolean> deleteBooking(Integer id);
}
