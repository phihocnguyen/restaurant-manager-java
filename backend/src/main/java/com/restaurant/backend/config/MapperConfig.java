package com.restaurant.backend.config;

import com.restaurant.backend.domains.dto.Account.dto.UpdateAccountDto;
import com.restaurant.backend.domains.dto.Receipt.dto.CreateReceiptDto;
import com.restaurant.backend.domains.dto.ReceiptDetail.ReceiptDetailDto;
import com.restaurant.backend.domains.dto.Recipe.RecipeDto;
import com.restaurant.backend.domains.dto.Recipe.dto.CreateRecipeDto;
import com.restaurant.backend.domains.dto.StockinDetailsDrinkOther.StockinDetailsDrinkOtherDto;
import com.restaurant.backend.domains.dto.StockinDetailsIngre.StockinDetailsIngreDto;
import com.restaurant.backend.domains.entities.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {
    @Bean
    ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration().setAmbiguityIgnored(true);

        // add this so you can add nested objects
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);

        modelMapper.typeMap(ReceiptDetail.class, ReceiptDetailDto.class).addMappings(mapper -> {
            mapper.map(src -> src.getRec(), ReceiptDetailDto::setRec);
            mapper.map(src -> src.getItem(), ReceiptDetailDto::setItem);
            mapper.map(src->src.getPrice(), ReceiptDetailDto::setPrice);
            mapper.map(src->src.getQuantity(), ReceiptDetailDto::setQuantity);
        });

        modelMapper.typeMap(Recipe.class, RecipeDto.class).addMappings(mapper -> {
           mapper.map(src -> src.getIngre(), RecipeDto::setIngre);
           mapper.map(src -> src.getItem(), RecipeDto::setItem);
           mapper.map(src -> src.getIngreQuantityKg(), RecipeDto::setIngreQuantityKg);
        });

        modelMapper.typeMap(StockinDetailsDrinkOther.class, StockinDetailsDrinkOtherDto.class).addMappings(mapper -> {
            mapper.map(src -> src.getSto(), StockinDetailsDrinkOtherDto::setSto);
            mapper.map(src -> src.getItem(), StockinDetailsDrinkOtherDto::setItem);
            mapper.map(src->src.getCprice(), StockinDetailsDrinkOtherDto::setCprice);
            mapper.map(src->src.getTotalCprice(), StockinDetailsDrinkOtherDto::setTotalCprice);
            mapper.map(src -> src.getQuantityUnits(), StockinDetailsDrinkOtherDto::setQuantityUnits);
        });

        modelMapper.typeMap(StockinDetailsIngre.class, StockinDetailsIngreDto.class).addMappings(mapper -> {
            mapper.map(src -> src.getIngre(), StockinDetailsIngreDto::setIngre);
            mapper.map(src -> src.getSto(), StockinDetailsIngreDto::setSto);
            mapper.map(src->src.getCprice(), StockinDetailsIngreDto::setCprice);
            mapper.map(src->src.getTotalCprice(), StockinDetailsIngreDto::setTotalCprice);
            mapper.map(src->src.getQuantityKg(), StockinDetailsIngreDto::setQuantityKg);
        });

        modelMapper.typeMap(UpdateAccountDto.class, Account.class)
                .addMappings(mapper -> {
                    mapper.skip(Account::setAccUsername);
                });

        return modelMapper;
    }
}

