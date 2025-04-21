package com.restaurant.backend.services.impl;

import com.restaurant.backend.domains.entities.DiningTable;
import com.restaurant.backend.repositories.DiningTableRepository;
import com.restaurant.backend.services.DiningTableService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DiningTableServiceImpl implements DiningTableService {
    private final DiningTableRepository diningTableRepository;
    public DiningTableServiceImpl(DiningTableRepository diningTableRepository) {
        this.diningTableRepository = diningTableRepository;
    }

    public DiningTable save(DiningTable diningTable) {
        return this.diningTableRepository.save(diningTable);
    }

    public DiningTable findOneById(int id) {
        return this.diningTableRepository.findOneById(id);
    }

    public List<DiningTable> findAll(){
        return this.diningTableRepository.findAll().stream()
                .filter(table -> table.getIsdeleted() == false).collect(Collectors.toList());
        // filter all the deleted one out
    }
}
