package com.restaurant.backend.config;

import com.restaurant.backend.domains.dto.ReceiptDetailDto;
import com.restaurant.backend.domains.dto.RecipeDto;
import com.restaurant.backend.domains.dto.StockinDetailsDrinkOtherDto;
import com.restaurant.backend.domains.dto.StockinDetailsIngreDto;
import com.restaurant.backend.domains.entities.ReceiptDetail;
import com.restaurant.backend.domains.entities.Recipe;
import com.restaurant.backend.domains.entities.StockinDetailsDrinkOther;
import com.restaurant.backend.domains.entities.StockinDetailsIngre;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {
    @Bean
    ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();
        // add this so you can add nested objects
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);

        modelMapper.typeMap(ReceiptDetail.class, ReceiptDetailDto.class).addMappings(mapper -> {
            mapper.map(src -> src.getRec(), ReceiptDetailDto::setRec);
            mapper.map(src -> src.getItem(), ReceiptDetailDto::setItem);
        });

        modelMapper.typeMap(Recipe.class, RecipeDto.class).addMappings(mapper -> {
           mapper.map(src -> src.getIngre(), RecipeDto::setIngre);
           mapper.map(src -> src.getItem(), RecipeDto::setItem);
        });

        modelMapper.typeMap(StockinDetailsDrinkOther.class, StockinDetailsDrinkOtherDto.class).addMappings(mapper -> {
            mapper.map(src -> src.getSto(), StockinDetailsDrinkOtherDto::setSto);
            mapper.map(src -> src.getItem(), StockinDetailsDrinkOtherDto::setItem);
        });

        modelMapper.typeMap(StockinDetailsIngre.class, StockinDetailsIngreDto.class).addMappings(mapper -> {
            mapper.map(src -> src.getIngre(), StockinDetailsIngreDto::setIngre);
            mapper.map(src -> src.getSto(), StockinDetailsIngreDto::setSto);
        });

        return modelMapper;
    }
}

