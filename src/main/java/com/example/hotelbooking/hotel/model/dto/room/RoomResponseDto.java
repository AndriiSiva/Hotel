package com.example.hotelbooking.hotel.model.dto.room;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomResponseDto {

    private Long id;

    private String roomName;

    private String roomDescription;

    private Short maximumRoomCapacity;

    private LocalDate dateAndWhenRoomWillBeOccupied;

    private LocalDate dateWhenRoomWillBeAvailable;

    private Integer roomPrice;
}
