package com.example.hotelbooking.statistics.repository;

import com.example.hotelbooking.statistics.model.BookingStatistics;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional
public interface BookingStatisticsRepository extends MongoRepository<BookingStatistics, String> {

    List<BookingStatistics> findBookingStatisticsByInAndOut(LocalDate in, LocalDate out);
}
