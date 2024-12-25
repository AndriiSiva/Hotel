package com.example.hotelbooking.user.service;

import com.example.hotelbooking.exception.exceptions.BadRequestException;
import com.example.hotelbooking.exception.exceptions.ObjectNotFoundException;
import com.example.hotelbooking.hotel.model.entity.Room;
import com.example.hotelbooking.hotel.repository.HotelRepository;
import com.example.hotelbooking.hotel.repository.RoomRepository;
import com.example.hotelbooking.user.mapper.BookingMapperManual;
import com.example.hotelbooking.user.model.dto.booking.BookingNewDto;
import com.example.hotelbooking.user.model.dto.booking.BookingResponseDto;
import com.example.hotelbooking.user.model.entity.Booking;
import com.example.hotelbooking.user.model.entity.User;
import com.example.hotelbooking.user.repository.BookingRepository;
import com.example.hotelbooking.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import static com.example.hotelbooking.user.mapper.BookingMapperManual.toBooking;
import static com.example.hotelbooking.user.mapper.BookingMapperManual.toBookingResponseDto;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final UserRepository userRepository;

    private final BookingRepository bookingRepository;

    private final HotelRepository hotelRepository;

    private final RoomRepository roomRepository;

    @Override
    public List<BookingResponseDto> sendAllUserAccountsBookingList(Long hotelId,
                                                                   Long roomId,
                                                                   LocalDate start,
                                                                   LocalDate end,
                                                                   PageRequest page) {

        return bookingRepository.getAllRoomsInHotelWhichAreInUse(hotelId,
                                                                 roomId,
                                                                 start,
                                                                 end,
                                                                 page)
                                                .stream()
                                                .map(BookingMapperManual::toBookingResponseDto)
                                                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BookingResponseDto createCheckIn(Long hotelId,
                                            Long roomId,
                                            Long userId,
                                            BookingNewDto newCheckIn) {
        User user = checkUserInDb(userId);
        checkHotelInDb(hotelId);
        Room room = checkRoomInDb(hotelId, roomId);

        if (room.getDateWhenRoomWillBeOccupied() == null || room.getDateWhenRoomWillBeAvailable() == null) {
            room.setDateWhenRoomWillBeOccupied(LocalDate.now());
            room.setDateWhenRoomWillBeAvailable(LocalDate.now());
        }

        if (this.checkBookingOnSelectedDate(hotelId, roomId, newCheckIn.getCheckInRoom())) {
            throw new BadRequestException("Room currently in use");
        }

        Booking newBook = toBooking(newCheckIn, user, room);
        user.setBookingList(List.of(newBook));
        bookingRepository.save(newBook);

        return toBookingResponseDto(newBook);
    }


    @Override
    public List<BookingResponseDto> sendListOfFreeRoomsOfCertainHotel(Long hotelId,
                                                                      Long roomId,
                                                                      LocalDate date,
                                                                      PageRequest page) {

        return checkRoomsAvailability(hotelId, roomId, date, page)
                .stream()
                .map(BookingMapperManual::toBookingResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingResponseDto> sendListOfThatOccupiedRoomsOfCertainHotel(Long hotelId,
                                                                              Long roomId,
                                                                              LocalDate targetDate,
                                                                              PageRequest page) {

        return checkRoomRoomsThatAreOccupied(hotelId, roomId, targetDate, page)
                .stream()
                .map(BookingMapperManual::toBookingResponseDto)
                .collect(Collectors.toList());
    }

    private List<Booking> checkRoomRoomsThatAreOccupied(Long hotelId,
                                                        Long roomId,
                                                        LocalDate targetDate,
                                                        PageRequest page) {

        return bookingRepository.checkRoomRoomsThatAreOccupied(hotelId, roomId, targetDate, page);
    }

    private Room checkRoomInDb(Long hotelId, Long roomId) {
        log.warn("No Room in selected hotel");

        return roomRepository.getRoom(hotelId, roomId).orElseThrow(() ->
                new ObjectNotFoundException("Room not present!"));
    }

    private void checkHotelInDb(Long hotelId) {
        log.warn("No Hotel for update");
        hotelRepository.findById(hotelId).orElseThrow(() ->
                new ObjectNotFoundException("Hotel not present!"));
    }

    private User checkUserInDb(Long userID) {
        log.warn("No User");

        return userRepository.findById(userID).orElseThrow(() ->
                new ObjectNotFoundException("User was not present"));
    }

    private List<Booking> checkRoomsAvailability(Long hotelId, Long roomId, LocalDate targetDate, PageRequest page) {

        return bookingRepository.getListOfRoomAvailableOnCurrentDate(hotelId, roomId, targetDate, page);
    }

    private Boolean checkBookingOnSelectedDate(Long hotelId, Long roomId, LocalDate checkInRoom) {
        Room room = checkRoomInDb(hotelId, roomId);
        if (room.getDateWhenRoomWillBeOccupied() == null || room.getDateWhenRoomWillBeAvailable() == null) {
            return true;
        }

        return checkInRoom.isBefore(room.getDateWhenRoomWillBeAvailable());
    }
}
