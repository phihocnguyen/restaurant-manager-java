package com.restaurant.backend.services;

import com.restaurant.backend.domains.dto.DiningTable.DiningTableDto;
import com.restaurant.backend.domains.dto.DiningTable.dto.CreateDiningTableDto;
import com.restaurant.backend.domains.entities.DiningTable;

import java.util.List;
import java.util.Optional;

public interface DiningTableService {
    public DiningTableDto createTable(CreateDiningTableDto dto);
    public DiningTableDto getTableById(int id);
    public List<DiningTableDto> getAllTables();
    public DiningTableDto updateTable(int id, CreateDiningTableDto dto);
    public DiningTableDto partialUpdateTable(int id, CreateDiningTableDto dto);
    public boolean softDeleteTable(int id);
    public DiningTable findById(int id);
}
