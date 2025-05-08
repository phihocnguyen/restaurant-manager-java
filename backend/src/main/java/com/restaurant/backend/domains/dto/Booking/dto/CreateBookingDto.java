package com.restaurant.backend.domains.dto.Booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookingDto {
    private Integer employeeId;
    private Integer customerId;
    private Integer tableId;
    private Instant startTime;
    private Instant endTime;
    private Short status = 0;
}