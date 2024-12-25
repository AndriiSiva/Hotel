package com.example.hotelbooking.hotel.model.dto.hotel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HotelNewDto {

    @Size(max=128)
    @NotBlank
    private String hotelName;

    @Size(max=128)
    @NotBlank
    private String displayName;

    @Size(max=128)
    @NotBlank
    private String city;

    @Size(max=256)
    @NotBlank
    private String hotelAddress;

    @NotBlank
    @Positive
    private Long distanceFromCenter;

    @Positive
    private Short hotelRating;
}
