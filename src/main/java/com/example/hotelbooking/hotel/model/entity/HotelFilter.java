package com.example.hotelbooking.hotel.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HotelFilter {

    private Long id;

    private String hotelName;

    private String displayName;

    private String city;

    private String hotelAddress;

    private Long distanceFromCenter;

    private Short hotelRating;

    private Short numberOfVotes;
}
