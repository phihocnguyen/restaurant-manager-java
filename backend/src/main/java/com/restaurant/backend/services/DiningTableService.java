package com.restaurant.backend.services;

import com.restaurant.backend.domains.entities.DiningTable;

import java.util.List;
import java.util.Optional;

public interface DiningTableService {
    public DiningTable save(DiningTable diningTable);
    public Optional<DiningTable> findById(int id);
    public List<DiningTable> findAll();
}
