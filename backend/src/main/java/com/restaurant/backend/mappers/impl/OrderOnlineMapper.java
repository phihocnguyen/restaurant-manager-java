package com.restaurant.backend.domains.dto.OrderOnline.mapper;

import com.restaurant.backend.domains.dto.OrderOnline.OrderOnline;
import com.restaurant.backend.domains.dto.OrderOnline.dto.OrderOnlineDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OrderOnlineMapper {
    @Mapping(target = "orderDetails", ignore = true)
    @Mapping(target = "employeeName", source = "employee.name")
    OrderOnlineDTO toDTO(OrderOnline orderOnline);

    @Mapping(target = "orderDetails", ignore = true)
    OrderOnline toEntity(OrderOnlineDTO orderOnlineDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderDetails", ignore = true)
    void updateEntityFromDTO(OrderOnlineDTO dto, @MappingTarget OrderOnline entity);
} 