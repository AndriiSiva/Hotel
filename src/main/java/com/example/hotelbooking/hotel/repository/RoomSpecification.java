package com.example.hotelbooking.hotel.repository;

import com.example.hotelbooking.hotel.model.entity.Room;
import com.example.hotelbooking.hotel.model.entity.RoomFilter;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import java.time.LocalDate;

public interface RoomSpecification {

    static Specification<Room> byRoomIdRoomNameInOutDatesAnRoomPrice(RoomFilter filter) {


        return Specification.where(byRoomId(filter.getId()))
                .and(byRoomName(filter.getRoomName()))
                .and(byRoomGuestMaximumRoomCapacity(filter.getMaximumRoomCapacity()))
                .and(RoomByHotelId(filter.getHotelId()))
                .and(RoomBetweenMinAndMaxPrice(filter.getRoomMinPrice(), filter.getRoomMaxPrice()))
                .and(RoomDateAvailableAndDateWillBeOccupied(filter.getDateWhenRoomWillBeOccupied(),
                        filter.getDateWhenRoomWillBeAvailable()));
    }

    static Specification<Room> byRoomId(Long roomId) {

        return (root, query, cb) -> {

            if (roomId == null) {
                return null;
            }

            return cb.equal(root.get("id"), roomId);
        };
    }

    static Specification<Room> byRoomName(String roomName) {

        return (root, query, cb) -> {

            if (roomName == null) {
                return null;
            }

            return cb.equal(root.get("roomName"), roomName);
        };
    }

    static Specification<Room> byRoomGuestMaximumRoomCapacity(Short roomCapacity) {

        return (root, query, cb) -> {

            if (roomCapacity == null) {
                return null;
            }

            return cb.equal(root.get("maximumRoomCapacity"), roomCapacity);
        };
    }

    static Specification<Room> RoomByHotelId(Long hotelId) {

        return (root, query, cb) -> {

            if (hotelId == null) {
                return null;
            }

            return cb.equal(root.get("hotel").get("id"), hotelId);
        };
    }

    static Specification<Room> RoomBetweenMinAndMaxPrice(Integer minPrice, Integer maxPrice) {

        return (root, query, cb) -> {

            if (minPrice == null && maxPrice == null) {
                return null;
            }

            if (minPrice == null) {
                return cb.lessThanOrEqualTo(root.get("roomPrice"), maxPrice);
            }

            if (maxPrice == null) {
                return cb.greaterThanOrEqualTo(root.get("roomPrice"), minPrice);
            }

            Predicate max = cb.gt(root.get("roomPrice"), maxPrice);
            Predicate min = cb.lt(root.get("roomPrice"), minPrice);

            return cb.between(root.get("roomPrice"), min, max);
        };
    }

    static Specification<Room> RoomDateAvailableAndDateWillBeOccupied(LocalDate whenRoomWillBeOccupied,
                                                                      LocalDate whenRoomWillBeAvailable) {

        return (root, query, cb) -> {

            if (whenRoomWillBeOccupied == null && whenRoomWillBeAvailable == null) {
                return null;
            }


            return cb.and(cb.greaterThanOrEqualTo(root.get("dateWhenRoomWillBeOccupied"), whenRoomWillBeOccupied),
                    cb.lessThanOrEqualTo(root.get("dateWhenRoomWillBeAvailable"), whenRoomWillBeAvailable));
        };
    }
}
