package com.example.hotelbooking.user.mapper;

import com.example.hotelbooking.hotel.model.entity.Room;
import com.example.hotelbooking.user.model.dto.booking.BookingNewDto;
import com.example.hotelbooking.user.model.dto.booking.BookingResponseDto;
import com.example.hotelbooking.user.model.entity.Booking;
import com.example.hotelbooking.user.model.entity.User;

public class BookingMapperManual {

    public static Booking toBooking(BookingNewDto newBooking, User booker, Room room) {

        return Booking.builder()
                .checkInRoom(newBooking.getCheckInRoom())
                .checkOutRoom(newBooking.getCheckOutRoom())
                .room(room)
                .user(booker)
                .build();
    }

    public static BookingResponseDto toBookingResponseDto(Booking booking) {

        return BookingResponseDto.builder()
                .id(booking.getId())
                .userId(booking.getUser().getId())
                .checkInRoom(booking.getCheckInRoom())
                .checkOutRoom(booking.getCheckOutRoom())
                .build();
    }
}
