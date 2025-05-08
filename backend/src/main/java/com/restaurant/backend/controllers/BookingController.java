package com.restaurant.backend.controllers;

import com.restaurant.backend.domains.dto.Booking.BookingDto;
import com.restaurant.backend.domains.dto.Booking.dto.CreateBookingDto;
import com.restaurant.backend.services.BookingService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<BookingDto> createBooking(@RequestBody CreateBookingDto dto) {
        return bookingService.createBooking(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingDto> getBooking(@PathVariable Integer id) {
        return bookingService.getBookingById(id);
    }

    @GetMapping
    public ResponseEntity<List<BookingDto>> getAllBookings() {
        return bookingService.getAllBookings();
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<BookingDto>> getBookingsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant end) {
        return bookingService.getBookingsByDateRange(start, end);
    }

    @GetMapping("/table/{tableId}/date-range")
    public ResponseEntity<List<BookingDto>> getBookingsByTableAndDateRange(
            @PathVariable Integer tableId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant end) {
        return bookingService.getBookingsByTableAndDateRange(tableId, start, end);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<BookingDto>> getBookingsByStatus(@PathVariable Short status) {
        return bookingService.getBookingsByStatus(status);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<BookingDto>> getBookingsByCustomer(@PathVariable Integer customerId) {
        return bookingService.getBookingsByCustomer(customerId);
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<BookingDto>> getBookingsByEmployee(@PathVariable Integer employeeId) {
        return bookingService.getBookingsByEmployee(employeeId);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<BookingDto> updateBookingStatus(
            @PathVariable Integer id,
            @RequestParam Short status) {
        return bookingService.updateBookingStatus(id, status);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteBooking(@PathVariable Integer id) {
        return bookingService.deleteBooking(id);
    }
}
