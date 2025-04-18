package com.restaurant.backend.mappers.impl;

import com.restaurant.backend.domains.dto.BookingDto;
import com.restaurant.backend.domains.entities.Booking;
import com.restaurant.backend.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class BookingMapper implements Mapper<Booking, BookingDto> {
    private ModelMapper modelMapper = new ModelMapper();
    public BookingMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public BookingDto mapFrom(Booking booking) {
        return modelMapper.map(booking, BookingDto.class);
    }

    @Override
    public Booking mapTo(BookingDto bookingDto) {
        return modelMapper.map(bookingDto, Booking.class);
    }
}
