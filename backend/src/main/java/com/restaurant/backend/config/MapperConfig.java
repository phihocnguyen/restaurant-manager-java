package com.restaurant.backend.config;

import com.restaurant.backend.domains.dto.Account.dto.UpdateAccountDto;
import com.restaurant.backend.domains.dto.MenuItem.dto.UpdateMenuItemDto;
import com.restaurant.backend.domains.dto.ReceiptDetail.ReceiptDetailDto;
import com.restaurant.backend.domains.dto.Recipe.RecipeDto;
import com.restaurant.backend.domains.dto.Receipt.ReceiptDto;
import com.restaurant.backend.domains.dto.DiningTable.DiningTableDto;
import com.restaurant.backend.domains.entities.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {
    @Bean
    ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration().setAmbiguityIgnored(true);

        // add this so you can add nested objects
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);

        modelMapper.typeMap(ReceiptDetail.class, ReceiptDetailDto.class).addMappings(mapper -> {
            mapper.map(src -> src.getRec().getId(), ReceiptDetailDto::setReceipt);
            mapper.map(src -> src.getItem(), ReceiptDetailDto::setMenuItem);
            mapper.map(src -> src.getQuantity(), ReceiptDetailDto::setQuantity);
            mapper.map(src -> src.getPrice(), ReceiptDetailDto::setPrice);
        });

        modelMapper.typeMap(Recipe.class, RecipeDto.class).addMappings(mapper -> {
            mapper.map(src -> src.getIngre(), RecipeDto::setIngredient);
            mapper.map(src -> src.getItem(), RecipeDto::setMenuItem);
            mapper.map(src -> src.getIngreQuantityKg(), RecipeDto::setIngreQuantityKg);
        });

        modelMapper.typeMap(Receipt.class, ReceiptDto.class).addMappings(mapper -> {
            mapper.map(src -> src.getCus(), ReceiptDto::setCustomer);
            mapper.map(src -> src.getEmp(), ReceiptDto::setEmployee);
            mapper.map(src -> src.getTab(), ReceiptDto::setDiningTable);
            mapper.map(src -> src.getRecPay(), ReceiptDto::setRecPay);
        });

        // Add mapping for DiningTable to DiningTableDto
        modelMapper.typeMap(DiningTable.class, DiningTableDto.class).addMappings(mapper -> {
            mapper.map(src -> src.getId(), DiningTableDto::setId); // Explicitly map ID
            mapper.map(src -> src.getTabNum(), DiningTableDto::setTabNum); // Explicitly map tabNum
            mapper.map(src -> src.getTabStatus(), DiningTableDto::setTabStatus);
            mapper.map(src -> src.getIsdeleted(), DiningTableDto::setIsdeleted);
        });

        modelMapper.typeMap(UpdateAccountDto.class, Account.class)
                .addMappings(mapper -> {
                    mapper.skip(Account::setAccUsername);
                });

        modelMapper.typeMap(UpdateMenuItemDto.class, MenuItem.class)
                .addMappings((mapper -> {
                    mapper.skip(MenuItem::setItemType);
                }));

        return modelMapper;
    }
}
