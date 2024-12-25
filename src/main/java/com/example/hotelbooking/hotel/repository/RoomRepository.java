package com.example.hotelbooking.hotel.repository;

import com.example.hotelbooking.hotel.model.entity.Room;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long>, JpaSpecificationExecutor<Room> {

    @Query(value = """
          SELECT *
          FROM ROOMS
          WHERE hotel_id = :hotelId
             AND id = :roomId
          """, nativeQuery = true)
    Optional<Room> getRoom(@Param ("hotelId")Long hotelId,
                           @Param("roomId") Long roomId);

    @Query(value = """
           SELECT
            r.id,
            r.available,
            r.occupied,
            r.capacity,
            r.description,
            r.name,
            r.price,
            r.hotel_id
           FROM ROOMS AS r
            JOIN HOTELS AS h ON r.hotel_id = h.id
           WHERE h.id = :hotelId
           """, nativeQuery = true)
    List<Room> getAllRoomsInHotel(@Param ("hotelId")Long hotelId, PageRequest page);
}
