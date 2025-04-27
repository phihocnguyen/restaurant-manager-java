package com.restaurant.backend.mappers.impl;

import com.restaurant.backend.domains.dto.Booking.BookingDto;
import com.restaurant.backend.domains.entities.Booking;
import com.restaurant.backend.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class BookingMapper implements Mapper<Booking, BookingDto> {
    private final ModelMapper modelMapper;

    public BookingMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public BookingDto mapFrom(Booking entity) {
        return modelMapper.map(entity, BookingDto.class);
    }

    @Override
    public Booking mapTo(BookingDto dto) {
        return modelMapper.map(dto, Booking.class);
    }
}
