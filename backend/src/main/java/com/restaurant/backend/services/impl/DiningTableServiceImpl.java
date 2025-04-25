package com.restaurant.backend.services.impl;

import com.restaurant.backend.domains.dto.DiningTable.DiningTableDto;
import com.restaurant.backend.domains.dto.DiningTable.dto.CreateDiningTableDto;
import com.restaurant.backend.domains.entities.DiningTable;
import com.restaurant.backend.mappers.impl.DiningTableMapper;
import com.restaurant.backend.repositories.DiningTableRepository;
import com.restaurant.backend.services.DiningTableService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DiningTableServiceImpl implements DiningTableService {
    private final DiningTableRepository diningTableRepository;
    private final DiningTableMapper diningTableMapper;

    public DiningTableServiceImpl(DiningTableRepository diningTableRepository, DiningTableMapper diningTableMapper) {
        this.diningTableRepository = diningTableRepository;
        this.diningTableMapper = diningTableMapper;
    }

    public DiningTableDto createTable(CreateDiningTableDto dto) {
        DiningTable entity = diningTableMapper.mapTo(dto);
        return diningTableMapper.mapFrom(diningTableRepository.save(entity));
    }

    public DiningTableDto getTableById(int id) {
        Optional<DiningTable> found = diningTableRepository.findById(id);
        return found.isPresent() ? diningTableMapper.mapFrom(found.get()) : null;
    }

    public List<DiningTableDto> getAllTables() {
        return diningTableRepository.findAll().stream()
                .filter(table -> !Boolean.TRUE.equals(table.getIsdeleted()))
                .map(diningTableMapper::mapFrom)
                .collect(Collectors.toList());
    }

    public DiningTableDto updateTable(int id, CreateDiningTableDto dto) {
        Optional<DiningTable> found = diningTableRepository.findById(id);
        if (!found.isPresent()) return null;

        DiningTable updated = diningTableMapper.mapTo(dto);
        updated.setId(id);
        return diningTableMapper.mapFrom(diningTableRepository.save(updated));
    }

    public DiningTableDto partialUpdateTable(int id, CreateDiningTableDto dto) {
        Optional<DiningTable> found = diningTableRepository.findById(id);
        if (!found.isPresent()) return null;

        DiningTable entity = found.get();
        if (dto.getTabNum() != null) entity.setTabNum(dto.getTabNum());
        if (dto.getTabStatus() != null) entity.setTabStatus(dto.getTabStatus());
        if (dto.getIsdeleted() != null) entity.setIsdeleted(dto.getIsdeleted());

        return diningTableMapper.mapFrom(diningTableRepository.save(entity));
    }

    public boolean softDeleteTable(int id) {
        Optional<DiningTable> found = diningTableRepository.findById(id);
        if (!found.isPresent()) return false;

        DiningTable entity = found.get();
        entity.setIsdeleted(true);
        diningTableRepository.save(entity);
        return true;
    }

    @Override
    public DiningTableDto findById(int id) {
        Optional<DiningTable> found = diningTableRepository.findById(id);
        return found.map(diningTableMapper::mapFrom).orElse(null);
    }
}

