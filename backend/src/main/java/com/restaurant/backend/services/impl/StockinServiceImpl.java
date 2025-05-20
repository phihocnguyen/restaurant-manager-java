package com.restaurant.backend.services.impl;

import com.restaurant.backend.domains.dto.Stockin.StockinDto;
import com.restaurant.backend.domains.dto.Stockin.StockInDetailsIngreDTO;
import com.restaurant.backend.domains.dto.Stockin.StockInDetailsDrinkOtherDTO;
import com.restaurant.backend.domains.entities.Ingredient;
import com.restaurant.backend.domains.entities.MenuItem;
import com.restaurant.backend.domains.entities.Stockin;
import com.restaurant.backend.domains.entities.StockinDetailsDrinkOther;
import com.restaurant.backend.domains.entities.StockinDetailsDrinkOtherId;
import com.restaurant.backend.domains.entities.StockinDetailsIngre;
import com.restaurant.backend.domains.entities.StockinDetailsIngreId;
import com.restaurant.backend.repositories.IngredientRepository;
import com.restaurant.backend.repositories.MenuItemRepository;
import com.restaurant.backend.repositories.StockinRepository;
import com.restaurant.backend.services.FinancialHistoryService;
import com.restaurant.backend.services.StockinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StockinServiceImpl implements StockinService {
    private final StockinRepository stockinRepository;
    private final FinancialHistoryService financialHistoryService;
    private final IngredientRepository ingredientRepository;
    private final MenuItemRepository menuItemRepository;

    @Autowired
    public StockinServiceImpl(StockinRepository stockinRepository,
            FinancialHistoryService financialHistoryService,
            IngredientRepository ingredientRepository,
            MenuItemRepository menuItemRepository) {
        this.stockinRepository = stockinRepository;
        this.financialHistoryService = financialHistoryService;
        this.ingredientRepository = ingredientRepository;
        this.menuItemRepository = menuItemRepository;
    }

    @Override
    @Transactional
    public StockinDto createStockIn(StockinDto stockinDto) {
        Stockin stockin = new Stockin();
        stockin.setDate(Instant.now());
        stockin.setPrice(stockinDto.getPrice().doubleValue());
        Stockin savedStockin = stockinRepository.save(stockin);

        // Create financial history record
        financialHistoryService.createStockInHistory(savedStockin.getId().longValue(), savedStockin.getPrice());

        if (stockinDto.getStockInDetailsIngres() != null) {
            List<StockinDetailsIngre> ingredients = stockinDto.getStockInDetailsIngres().stream()
                    .map(dto -> {
                        StockinDetailsIngre detail = new StockinDetailsIngre();
                        detail.setStockin(savedStockin);
                        detail.setQuantityKg(dto.getQuantityKg());
                        detail.setCPrice(dto.getCPrice());
                        if (dto.getCPrice() != null && dto.getQuantityKg() != null) {
                            detail.setTotalCPrice(dto.getCPrice() * dto.getQuantityKg());
                        }

                        Ingredient ingredient = ingredientRepository.findById(dto.getIngredientId())
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                        "Ingredient not found"));
                        detail.setIngredient(ingredient);

                        StockinDetailsIngreId id = new StockinDetailsIngreId();
                        id.setStoId(savedStockin.getId());
                        id.setIngreId(dto.getIngredientId());
                        detail.setId(id);

                        return detail;
                    })
                    .collect(Collectors.toList());
            savedStockin.setStockinDetailsIngres(ingredients);
        }

        if (stockinDto.getStockInDetailsDrinkOthers() != null) {
            List<StockinDetailsDrinkOther> drinkOthers = stockinDto.getStockInDetailsDrinkOthers().stream()
                    .map(dto -> {
                        StockinDetailsDrinkOther detail = new StockinDetailsDrinkOther();
                        detail.setStockin(savedStockin);
                        detail.setQuantityUnits(dto.getQuantityUnits().doubleValue());
                        detail.setCPrice(dto.getCPrice().doubleValue());
                        if (dto.getCPrice() != null && dto.getQuantityUnits() != null) {
                            detail.setTotalCPrice(
                                    dto.getCPrice().multiply(BigDecimal.valueOf(dto.getQuantityUnits())).doubleValue());
                        }

                        MenuItem menuItem = menuItemRepository.findById(dto.getItemId())
                                .orElseThrow(
                                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "MenuItem not found"));
                        detail.setMenuItem(menuItem);

                        StockinDetailsDrinkOtherId id = new StockinDetailsDrinkOtherId();
                        id.setStoId(savedStockin.getId());
                        id.setItemId(dto.getItemId());
                        detail.setId(id);

                        return detail;
                    })
                    .collect(Collectors.toList());
            savedStockin.setStockinDetailsDrinkOthers(drinkOthers);
        }

        Stockin finalStockin = stockinRepository.save(savedStockin);
        return convertToDTO(finalStockin);
    }

    @Override
    @Transactional
    public StockinDto updateStockIn(Integer id, StockinDto stockinDto) {
        Stockin existingStockin = stockinRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Stockin not found"));

        existingStockin.setPrice(stockinDto.getPrice().doubleValue());

        if (stockinDto.getStockInDetailsIngres() != null) {
            List<StockinDetailsIngre> newIngredients = new ArrayList<>();
            for (StockInDetailsIngreDTO dto : stockinDto.getStockInDetailsIngres()) {
                StockinDetailsIngre detail = new StockinDetailsIngre();
                detail.setStockin(existingStockin);
                detail.setQuantityKg(dto.getQuantityKg());
                detail.setCPrice(dto.getCPrice());
                if (dto.getCPrice() != null && dto.getQuantityKg() != null) {
                    detail.setTotalCPrice(dto.getCPrice() * dto.getQuantityKg());
                }

                Ingredient ingredient = ingredientRepository.findById(dto.getIngredientId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ingredient not found"));
                detail.setIngredient(ingredient);

                StockinDetailsIngreId detailId = new StockinDetailsIngreId();
                detailId.setStoId(existingStockin.getId());
                detailId.setIngreId(dto.getIngredientId());
                detail.setId(detailId);

                newIngredients.add(detail);
            }
            existingStockin.getStockinDetailsIngres().clear();
            existingStockin.getStockinDetailsIngres().addAll(newIngredients);
        }

        if (stockinDto.getStockInDetailsDrinkOthers() != null) {
            List<StockinDetailsDrinkOther> newDrinkOthers = new ArrayList<>();
            for (StockInDetailsDrinkOtherDTO dto : stockinDto.getStockInDetailsDrinkOthers()) {
                StockinDetailsDrinkOther detail = new StockinDetailsDrinkOther();
                detail.setStockin(existingStockin);
                detail.setQuantityUnits(dto.getQuantityUnits().doubleValue());
                detail.setCPrice(dto.getCPrice().doubleValue());
                if (dto.getCPrice() != null && dto.getQuantityUnits() != null) {
                    detail.setTotalCPrice(
                            dto.getCPrice().multiply(BigDecimal.valueOf(dto.getQuantityUnits())).doubleValue());
                }

                MenuItem menuItem = menuItemRepository.findById(dto.getItemId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "MenuItem not found"));
                detail.setMenuItem(menuItem);

                StockinDetailsDrinkOtherId detailId = new StockinDetailsDrinkOtherId();
                detailId.setStoId(existingStockin.getId());
                detailId.setItemId(dto.getItemId());
                detail.setId(detailId);

                newDrinkOthers.add(detail);
            }
            existingStockin.getStockinDetailsDrinkOthers().clear();
            existingStockin.getStockinDetailsDrinkOthers().addAll(newDrinkOthers);
        }

        Stockin updatedStockin = stockinRepository.save(existingStockin);
        return convertToDTO(updatedStockin);
    }

    @Override
    @Transactional
    public void deleteStockIn(Integer id) {
        Stockin stockin = stockinRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Stockin not found"));
        stockinRepository.delete(stockin);
    }

    @Override
    public List<StockinDto> getAllStockIns() {
        return stockinRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public StockinDto getStockInById(Integer id) {
        Stockin stockin = stockinRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Stockin not found"));
        return convertToDTO(stockin);
    }

    private StockinDto convertToDTO(Stockin stockin) {
        StockinDto dto = new StockinDto();
        dto.setId(stockin.getId());
        dto.setDate(stockin.getDate());
        dto.setPrice(BigDecimal.valueOf(stockin.getPrice()));

        if (stockin.getStockinDetailsIngres() != null) {
            dto.setStockInDetailsIngres(stockin.getStockinDetailsIngres().stream()
                    .map(detail -> {
                        StockInDetailsIngreDTO detailDTO = new StockInDetailsIngreDTO();
                        detailDTO.setStockInId(detail.getStockin().getId());
                        detailDTO.setIngredientId(detail.getId().getIngreId());
                        detailDTO.setQuantityKg(detail.getQuantityKg());
                        detailDTO.setCPrice(detail.getCPrice());
                        detailDTO.setTotalCPrice(detail.getTotalCPrice());
                        return detailDTO;
                    })
                    .collect(Collectors.toList()));
        }

        if (stockin.getStockinDetailsDrinkOthers() != null) {
            dto.setStockInDetailsDrinkOthers(stockin.getStockinDetailsDrinkOthers().stream()
                    .map(detail -> {
                        StockInDetailsDrinkOtherDTO detailDTO = new StockInDetailsDrinkOtherDTO();
                        detailDTO.setStockInId(detail.getStockin().getId());
                        detailDTO.setItemId(detail.getId().getItemId());
                        detailDTO.setQuantityUnits(detail.getQuantityUnits().intValue());
                        detailDTO.setCPrice(BigDecimal.valueOf(detail.getCPrice()));
                        detailDTO.setTotalCPrice(BigDecimal.valueOf(detail.getTotalCPrice()));
                        return detailDTO;
                    })
                    .collect(Collectors.toList()));
        }

        return dto;
    }
}