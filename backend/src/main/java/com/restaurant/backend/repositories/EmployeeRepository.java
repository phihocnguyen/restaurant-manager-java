package com.restaurant.backend.repositories;

import com.restaurant.backend.domains.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    Optional<Employee> findByCccd(String cccd);

    Optional<Employee> findByPhone(String phone);

    List<Employee> findByRoleAndIsdeletedFalse(String role);

    List<Employee> findByIsdeletedFalse();
}
