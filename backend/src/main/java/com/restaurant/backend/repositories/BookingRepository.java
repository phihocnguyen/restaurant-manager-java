package com.restaurant.backend.repositories;

import com.restaurant.backend.domains.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findByStartTimeBetweenAndIsdeletedFalse(Instant start, Instant end);

    List<Booking> findByTableIdAndStartTimeBetweenAndIsdeletedFalse(Integer tableId, Instant start, Instant end);

    List<Booking> findByStatusAndIsdeletedFalse(Short status);

    List<Booking> findByCustomerIdAndIsdeletedFalse(Integer customerId);

    List<Booking> findByEmployeeIdAndIsdeletedFalse(Integer employeeId);
}
