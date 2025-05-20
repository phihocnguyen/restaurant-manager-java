package com.restaurant.backend.config;

import com.restaurant.backend.domains.dto.Account.dto.UpdateAccountDto;
import com.restaurant.backend.domains.dto.MenuItem.dto.UpdateMenuItemDto;
import com.restaurant.backend.domains.dto.ReceiptDetail.ReceiptDetailDto;
import com.restaurant.backend.domains.dto.Recipe.RecipeDto;
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
            mapper.map(src -> src.getRec(), ReceiptDetailDto::setRec);
            mapper.map(src -> src.getItem(), ReceiptDetailDto::setItem);
            mapper.map(src -> src.getPrice(), ReceiptDetailDto::setPrice);
            mapper.map(src -> src.getQuantity(), ReceiptDetailDto::setQuantity);
        });

        modelMapper.typeMap(Recipe.class, RecipeDto.class).addMappings(mapper -> {
            mapper.map(src -> src.getIngre(), RecipeDto::setIngre);
            mapper.map(src -> src.getItem(), RecipeDto::setItem);
            mapper.map(src -> src.getIngreQuantityKg(), RecipeDto::setIngreQuantityKg);
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
