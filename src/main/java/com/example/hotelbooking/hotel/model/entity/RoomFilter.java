package com.example.hotelbooking.hotel.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomFilter {

    private Long id;

    private Long hotelId;

    private String roomName;

    private Integer roomMaxPrice;

    private Integer roomMinPrice;

    private Short maximumRoomCapacity;

    private LocalDate dateWhenRoomWillBeOccupied;

    private LocalDate dateWhenRoomWillBeAvailable;
}
