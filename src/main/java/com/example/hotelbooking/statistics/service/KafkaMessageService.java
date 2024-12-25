package com.example.hotelbooking.statistics.service;

import com.example.hotelbooking.exception.exceptions.BadRequestException;
import com.example.hotelbooking.exception.exceptions.ObjectNotFoundException;
import com.example.hotelbooking.statistics.model.BookingStatistics;
import com.example.hotelbooking.statistics.model.KafkaMessage;
import com.example.hotelbooking.statistics.model.UserStatistics;
import com.example.hotelbooking.statistics.repository.BookingStatisticsRepository;
import com.example.hotelbooking.statistics.repository.UserStatisticsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaMessageService {

    private final UserStatisticsRepository userStatisticsRepository;

    private final BookingStatisticsRepository bookingStatisticsRepository;

    public void saveInDbUserStatistics(KafkaMessage message) {

        String type = message.getType();

        if (!type.equals("user-statistics")) {
            throw new  BadRequestException("Bad message type error!");
        }

        if (message.getMessage() == null) {
            throw new ObjectNotFoundException("Bad payload error!");
        }

        UserStatistics userStatistics = new UserStatistics();

        userStatistics.setUserId(Long.parseLong(message.getMessage().get(0)));
        userStatisticsRepository.save(userStatistics);
    }

    public void saveInDbBookingStatistics(KafkaMessage message) {

        String type = message.getType();

        if (!type.equals("booking-statistics")) {
            throw new  BadRequestException("Bad message type error!");
        }

        if (message.getMessage() == null) {
            throw new ObjectNotFoundException("Bad payload error!");
        }

        BookingStatistics bookingStatistics = new BookingStatistics();

        bookingStatistics.setUseId(Long.parseLong(message.getMessage().get(0)));
        bookingStatistics.setIn(LocalDate.parse(message.getMessage().get(1)));
        bookingStatistics.setOut(LocalDate.parse(message.getMessage().get(2)));

        bookingStatisticsRepository.save(bookingStatistics);
    }
}