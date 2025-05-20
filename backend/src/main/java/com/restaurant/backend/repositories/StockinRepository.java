package com.restaurant.backend.repositories;

import com.restaurant.backend.domains.entities.Stockin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockinRepository extends JpaRepository<Stockin, Integer> {
}