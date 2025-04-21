package com.restaurant.backend.services;

import com.restaurant.backend.domains.entities.DiningTable;

import java.util.List;

public interface DiningTableService {
    public DiningTable save(DiningTable diningTable);
    public DiningTable findOneById(int id);
    public List<DiningTable> findAll();
}
