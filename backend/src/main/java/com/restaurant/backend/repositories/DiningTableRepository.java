package com.restaurant.backend.repositories;

import com.restaurant.backend.domains.entities.DiningTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiningTableRepository extends JpaRepository<DiningTable, Integer> {
}
