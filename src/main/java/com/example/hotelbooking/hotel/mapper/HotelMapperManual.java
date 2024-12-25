package com.example.hotelbooking.hotel.mapper;

import com.example.hotelbooking.hotel.model.dto.hotel.HotelNewDto;
import com.example.hotelbooking.hotel.model.dto.hotel.HotelResponseDto;
import com.example.hotelbooking.hotel.model.entity.Hotel;
import java.util.ArrayList;

public class HotelMapperManual {

    public static Hotel toHotel(HotelNewDto newHotelDto) {

        return Hotel.builder()
                .hotelName(newHotelDto.getHotelName())
                .displayName(newHotelDto.getDisplayName())
                .city(newHotelDto.getCity())
                .hotelAddress(newHotelDto.getHotelAddress())
                .distanceFromCenter(newHotelDto.getDistanceFromCenter())
                .numberOfVotes(null)
                .hotelRating(newHotelDto.getHotelRating())
                .listOfAvailableRoomsToBook(new ArrayList<>())
                .build();
    }

    public static HotelResponseDto hotelResponseDto(Hotel responseHotelDto) {

        return HotelResponseDto.builder()
                .id(responseHotelDto.getId())
                .hotelName(responseHotelDto.getHotelName())
                .displayName(responseHotelDto.getDisplayName())
                .city(responseHotelDto.getCity())
                .hotelAddress(responseHotelDto.getHotelAddress())
                .distanceFromCenter(responseHotelDto.getDistanceFromCenter())
                .numberOfVotes(responseHotelDto.getNumberOfVotes())
                .hotelRating(responseHotelDto.getHotelRating())
                .build();
    }
}
