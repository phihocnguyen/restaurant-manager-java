package com.restaurant.backend.repositories;

import com.restaurant.backend.domains.entities.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, Integer> {
    @EntityGraph(attributePaths = "recDetails")
    List<Receipt> findAll();
}
