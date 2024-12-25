package com.example.hotelbooking.hotel.service;

import com.example.hotelbooking.exception.exceptions.ObjectNotFoundException;
import com.example.hotelbooking.hotel.mapper.RoomMapperManual;
import com.example.hotelbooking.hotel.model.dto.room.RoomNewDto;
import com.example.hotelbooking.hotel.model.dto.room.RoomResponseDto;
import com.example.hotelbooking.hotel.model.entity.Hotel;
import com.example.hotelbooking.hotel.model.entity.Room;
import com.example.hotelbooking.hotel.model.entity.RoomFilter;
import com.example.hotelbooking.hotel.repository.HotelRepository;
import com.example.hotelbooking.hotel.repository.RoomRepository;
import com.example.hotelbooking.hotel.repository.RoomSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import static com.example.hotelbooking.hotel.mapper.RoomMapperManual.toRoom;
import static com.example.hotelbooking.hotel.mapper.RoomMapperManual.toRoomResponseDto;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final HotelRepository hotelRepository;

    private final RoomRepository roomRepository;

    @Override
    public List<RoomResponseDto> filteredByCriteria(Long hotelId, RoomFilter filter, PageRequest page) {
        filter.setHotelId(hotelId);

        return roomRepository.findAll(RoomSpecification.byRoomIdRoomNameInOutDatesAnRoomPrice(filter), page)
                .stream()
                .map(RoomMapperManual::toRoomResponseDto)
                .collect(Collectors.toList());

    }

    @Override
    public List<RoomResponseDto> getRoomsInHotelList(Long hotelId, PageRequest page) {

        return roomRepository.getAllRoomsInHotel(hotelId, page)
                .stream()
                .map(RoomMapperManual::toRoomResponseDto)
                .collect(Collectors.toList());
    }


    @Override
    public RoomResponseDto getRoomById(Long hotelId, Long roomId) {
        Hotel hotel = checkHotelInDb(hotelId);

        Room room = checkRoomInDb(hotelId, roomId);

        if (!hotel.getListOfAvailableRoomsToBook().contains(room)) {
            throw new ObjectNotFoundException("There no room in hotel!");
        }

        log.info(("\nHotel with hotelId: %d which containing room with roomId: %d" +
                " was sent via rooms service at time: ")
                .formatted(hotelId, roomId)
                + LocalDateTime.now() + "\n");

        return toRoomResponseDto(room);
    }

    @Override
    @Transactional
    public RoomResponseDto creatNewRoomInHotel(Long hotelId, RoomNewDto newRoomInHotel) {
        Hotel hotel = checkHotelInDb(hotelId);
        Room newRoom = toRoom(newRoomInHotel, hotel);
        hotel.setListOfAvailableRoomsToBook(List.of(newRoom));

        newRoom = roomRepository.save(newRoom);

        log.info("\nRoom in hotel with hotelId; %d was created via rooms service at time: "
                .formatted(hotelId) + LocalDateTime.now() + "\n");

        return toRoomResponseDto(newRoom);
    }

    @Override
    @Transactional
    public RoomResponseDto updateRoomInfoInHotel(Long hotelId,
                                                 Long roomId,
                                                 RoomNewDto roomToUpdateInHotel) {
        Room room = checkRoomInDb(hotelId, roomId);
        Hotel hotel = checkHotelInDb(hotelId);

        if (roomToUpdateInHotel != null || hotel.getListOfAvailableRoomsToBook().contains(room)) {

            if (StringUtils.hasText(roomToUpdateInHotel.getRoomName())) {
                room.setRoomName(roomToUpdateInHotel.getRoomName());
            }

            if (StringUtils.hasText(roomToUpdateInHotel.getRoomDescription())) {
                room.setRoomDescription(roomToUpdateInHotel.getRoomDescription());
            }

            if (roomToUpdateInHotel.getMaximumRoomCapacity() != null) {
                room.setMaximumRoomCapacity(roomToUpdateInHotel.getMaximumRoomCapacity());
            }

            if (roomToUpdateInHotel.getDateWhenRoomWillBeAvailable() != null &&
                    roomToUpdateInHotel.getDateWhenRoomWillBeOccupied()
                            .isAfter(LocalDate.now()) &&
                    roomToUpdateInHotel.getDateWhenRoomWillBeAvailable()
                            .isAfter(roomToUpdateInHotel.getDateWhenRoomWillBeOccupied())) {
                room.setDateWhenRoomWillBeAvailable(roomToUpdateInHotel.getDateWhenRoomWillBeAvailable());
            }

            if (roomToUpdateInHotel.getDateWhenRoomWillBeOccupied() != null &&
                    roomToUpdateInHotel.getDateWhenRoomWillBeOccupied().isAfter(LocalDate.now()) &&
                    roomToUpdateInHotel.getDateWhenRoomWillBeOccupied()
                            .isBefore(roomToUpdateInHotel.getDateWhenRoomWillBeOccupied())) {
                room.setDateWhenRoomWillBeOccupied(roomToUpdateInHotel.getDateWhenRoomWillBeOccupied());
            }

            if (roomToUpdateInHotel.getRoomPrice() != null) {
                room.setRoomPrice(roomToUpdateInHotel.getRoomPrice());
            }

        } else {
            log.warn("No room fields for update");
            throw new ObjectNotFoundException("No room fields for update");
        }

        log.info(("\nRoom with roomId: %d  in hotel with hotelId:" +
                " %d was updated via rooms service at time: ").formatted(roomId, hotelId) +
                LocalDateTime.now() + "\n");

        return toRoomResponseDto(roomRepository.save(room));
    }

    @Override
    @Transactional
    public RoomResponseDto removeRoomInHotelByRoomId(Long hotelId, Long roomId) {
        Room room = checkRoomInDb(hotelId, roomId);
        roomRepository.findById(roomId).ifPresent(roomRepository::delete);

        log.info(("\nRoom with roomId: %d in hotel with hotelId: %d" +
                " was deleted via rooms service at time: ").formatted(roomId, hotelId)
                + LocalDateTime.now() + "\n");

        return toRoomResponseDto(room);
    }

    private Room checkRoomInDb(Long hotelId, Long roomId) {
        log.warn("No Room in selected hotel");
        return roomRepository.getRoom(hotelId,roomId).orElseThrow(() ->
                new ObjectNotFoundException("Room not present!"));
    }

    private Hotel checkHotelInDb(Long hotelId) {
        log.warn("No Hotel for update");
        return hotelRepository.findById(hotelId).orElseThrow(() ->
                new ObjectNotFoundException("Hotel not present!"));
    }
}
