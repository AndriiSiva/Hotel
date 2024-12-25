package com.example.hotelbooking.hotel.model.dto.room;

import com.example.hotelbooking.annotation.BeforeReleaseDate;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomNewDto {

    @Size(max=64)
    @NotBlank
    private String roomName;

    @Size(max=256)
    @NotBlank
    private String roomDescription;

    @NotBlank
    @Positive
    private Short maximumRoomCapacity;

    @BeforeReleaseDate
    private LocalDate dateWhenRoomWillBeOccupied;

    @Future
    private LocalDate dateWhenRoomWillBeAvailable;

    @NotBlank
    @Positive
    private Integer roomPrice;
}
