package com.restaurant.backend.domains.dto.Booking;

import com.restaurant.backend.domains.dto.Customer.CustomerDto;
import com.restaurant.backend.domains.dto.DiningTable.DiningTableDto;
import com.restaurant.backend.domains.dto.Employee.EmployeeDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {
    private Integer id;
    private EmployeeDto employee;
    private CustomerDto customer;
    private DiningTableDto table;
    private Instant startTime;
    private Instant endTime;
    private Short status; // 0: PENDING, 1: CONFIRMED, 2: CANCELLED
    private Boolean isdeleted;
}
