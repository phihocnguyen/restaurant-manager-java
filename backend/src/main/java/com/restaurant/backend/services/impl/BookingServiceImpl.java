package com.restaurant.backend.services.impl;

import com.restaurant.backend.domains.dto.Booking.BookingDto;
import com.restaurant.backend.domains.dto.Booking.dto.CreateBookingDto;
import com.restaurant.backend.domains.entities.Booking;
import com.restaurant.backend.domains.entities.Customer;
import com.restaurant.backend.domains.entities.DiningTable;
import com.restaurant.backend.domains.entities.Employee;
import com.restaurant.backend.mappers.impl.BookingMapper;
import com.restaurant.backend.repositories.BookingRepository;
import com.restaurant.backend.repositories.CustomerRepository;
import com.restaurant.backend.repositories.DiningTableRepository;
import com.restaurant.backend.repositories.EmployeeRepository;
import com.restaurant.backend.services.BookingService;
import com.restaurant.backend.domains.dto.DiningTable.enums.TableStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final DiningTableRepository diningTableRepository;
    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;
    private final BookingMapper bookingMapper;

    public BookingServiceImpl(BookingRepository bookingRepository,
            DiningTableRepository diningTableRepository,
            CustomerRepository customerRepository,
            EmployeeRepository employeeRepository,
            BookingMapper bookingMapper) {
        this.bookingRepository = bookingRepository;
        this.diningTableRepository = diningTableRepository;
        this.customerRepository = customerRepository;
        this.employeeRepository = employeeRepository;
        this.bookingMapper = bookingMapper;
    }

    @Override
    public ResponseEntity<BookingDto> createBooking(CreateBookingDto dto) {
        DiningTable table = diningTableRepository.findById(dto.getTableId())
                .orElseThrow(() -> new RuntimeException("Table not found"));

        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        Booking booking = new Booking();
        booking.setEmployee(employee);
        booking.setCustomer(customer);
        booking.setTable(table);
        booking.setStartTime(dto.getStartTime());
        booking.setEndTime(dto.getEndTime());
        booking.setStatus(dto.getStatus());
        booking.setIsdeleted(false);

        Booking saved = bookingRepository.save(booking);
        return new ResponseEntity<>(bookingMapper.mapFrom(saved), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<BookingDto> getBookingById(Integer id) {
        return bookingRepository.findById(id)
                .map(booking -> new ResponseEntity<>(bookingMapper.mapFrom(booking), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Override
    public ResponseEntity<List<BookingDto>> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();
        List<BookingDto> dtos = bookings.stream()
                .map(bookingMapper::mapFrom)
                .collect(Collectors.toList());
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<BookingDto>> getBookingsByDateRange(Instant start, Instant end) {
        List<Booking> bookings = bookingRepository.findByStartTimeBetweenAndIsdeletedFalse(start, end);
        List<BookingDto> dtos = bookings.stream()
                .map(bookingMapper::mapFrom)
                .collect(Collectors.toList());
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<BookingDto>> getBookingsByTableAndDateRange(Integer tableId, Instant start,
            Instant end) {
        List<Booking> bookings = bookingRepository.findByTableIdAndStartTimeBetweenAndIsdeletedFalse(tableId, start,
                end);
        List<BookingDto> dtos = bookings.stream()
                .map(bookingMapper::mapFrom)
                .collect(Collectors.toList());
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<BookingDto>> getBookingsByStatus(Short status) {
        List<Booking> bookings = bookingRepository.findByStatusAndIsdeletedFalse(status);
        List<BookingDto> dtos = bookings.stream()
                .map(bookingMapper::mapFrom)
                .collect(Collectors.toList());
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<BookingDto>> getBookingsByCustomer(Integer customerId) {
        List<Booking> bookings = bookingRepository.findByCustomerIdAndIsdeletedFalse(customerId);
        List<BookingDto> dtos = bookings.stream()
                .map(bookingMapper::mapFrom)
                .collect(Collectors.toList());
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<BookingDto>> getBookingsByEmployee(Integer employeeId) {
        List<Booking> bookings = bookingRepository.findByEmployeeIdAndIsdeletedFalse(employeeId);
        List<BookingDto> dtos = bookings.stream()
                .map(bookingMapper::mapFrom)
                .collect(Collectors.toList());
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<BookingDto> updateBookingStatus(Integer id, Short status) {
        return bookingRepository.findById(id)
                .map(booking -> {
                    booking.setStatus(status);
                    Booking updated = bookingRepository.save(booking);
                    
                    // Update table status when booking is confirmed
                    if (status == 1) { // CONFIRMED
                        DiningTable table = booking.getTable();
                        table.setTabStatus(TableStatus.RESERVED);
                        diningTableRepository.save(table);
                    } else if (status == 2) { // CANCELLED
                        DiningTable table = booking.getTable();
                        table.setTabStatus(TableStatus.EMPTY);
                        diningTableRepository.save(table);
                    }
                    
                    return new ResponseEntity<>(bookingMapper.mapFrom(updated), HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Override
    public ResponseEntity<Boolean> deleteBooking(Integer id) {
        return bookingRepository.findById(id)
                .map(booking -> {
                    booking.setIsdeleted(true);
                    bookingRepository.save(booking);
                    return new ResponseEntity<>(true, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(false, HttpStatus.NOT_FOUND));
    }
}
