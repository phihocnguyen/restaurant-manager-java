package com.restaurant.backend.repositories;

import com.restaurant.backend.domains.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    boolean existsByCccd(String cccd);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);
    
    Customer findByEmail(String email);
}
