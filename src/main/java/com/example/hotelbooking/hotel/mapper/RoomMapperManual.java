package com.example.hotelbooking.hotel.mapper;

import com.example.hotelbooking.hotel.model.dto.room.RoomNewDto;
import com.example.hotelbooking.hotel.model.dto.room.RoomResponseDto;
import com.example.hotelbooking.hotel.model.entity.Hotel;
import com.example.hotelbooking.hotel.model.entity.Room;
import java.util.ArrayList;

public class RoomMapperManual {

    public static Room toRoom(RoomNewDto newRoom, Hotel hotel) {

        return Room.builder()
                .roomName(newRoom.getRoomName())
                .roomDescription(newRoom.getRoomDescription())
                .maximumRoomCapacity(newRoom.getMaximumRoomCapacity())
                .dateWhenRoomWillBeOccupied(newRoom.getDateWhenRoomWillBeOccupied())
                .dateWhenRoomWillBeAvailable(newRoom.getDateWhenRoomWillBeAvailable())
                .roomPrice(newRoom.getRoomPrice())
                .hotel(hotel)
                .bookingList(new ArrayList<>())
                .build();
    }

    public static RoomResponseDto toRoomResponseDto(Room roomResponse) {

        return RoomResponseDto.builder()
                .id(roomResponse.getId())
                .roomName(roomResponse.getRoomName())
                .roomDescription(roomResponse.getRoomDescription())
                .maximumRoomCapacity(roomResponse.getMaximumRoomCapacity())
                .dateAndWhenRoomWillBeOccupied(roomResponse.getDateWhenRoomWillBeOccupied())
                .dateWhenRoomWillBeAvailable(roomResponse.getDateWhenRoomWillBeAvailable())
                .roomPrice(roomResponse.getRoomPrice())
                .build();
    }
}
