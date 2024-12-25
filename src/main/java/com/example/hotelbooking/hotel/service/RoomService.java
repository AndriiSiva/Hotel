package com.example.hotelbooking.hotel.service;

import com.example.hotelbooking.hotel.model.dto.room.RoomNewDto;
import com.example.hotelbooking.hotel.model.dto.room.RoomResponseDto;
import com.example.hotelbooking.hotel.model.entity.RoomFilter;
import org.springframework.data.domain.PageRequest;
import java.util.List;

public interface RoomService {
    List<RoomResponseDto> filteredByCriteria(Long hotelId,
                                             RoomFilter filter, PageRequest page);

    List<RoomResponseDto> getRoomsInHotelList(Long hotelId,
                                              PageRequest page);

    RoomResponseDto getRoomById(Long hotelId,
                                Long roomId);

    RoomResponseDto creatNewRoomInHotel(Long hotelId,
                                        RoomNewDto newRoomInHotel);

    RoomResponseDto updateRoomInfoInHotel(Long hotelId,
                                          Long roomId,
                                          RoomNewDto roomToUpdateInHotel);

    RoomResponseDto removeRoomInHotelByRoomId(Long hotelId,
                                              Long roomId);
}
