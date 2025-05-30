package com.restaurant.backend.domains.dto.OrderOnline.mapper;

import com.restaurant.backend.domains.dto.OrderOnline.OrderOnlineDetails;
import com.restaurant.backend.domains.dto.OrderOnline.dto.OrderOnlineDetailsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OrderOnlineDetailsMapper {
    @Mapping(target = "orderOnlineId", source = "orderOnline.id")
    OrderOnlineDetailsDTO toDTO(OrderOnlineDetails orderOnlineDetails);

    @Mapping(target = "orderOnline", ignore = true)
    OrderOnlineDetails toEntity(OrderOnlineDetailsDTO orderOnlineDetailsDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderOnline", ignore = true)
    void updateEntityFromDTO(OrderOnlineDetailsDTO dto, @MappingTarget OrderOnlineDetails entity);
} 