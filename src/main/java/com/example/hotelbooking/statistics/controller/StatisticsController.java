package com.example.hotelbooking.statistics.controller;

import com.example.hotelbooking.statistics.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;

@RestController
@RequestMapping("/hotel-booking/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService bookingStatistics;

    @GetMapping("/sendUser")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public String getUserStatistics() {

        return bookingStatistics.sendUserStatistics();
    }

    @GetMapping("/sendBooking")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public String geBookingStatistics() {

        return bookingStatistics.sendBookingStatisticsForAllTime();
    }

    @GetMapping("/sendBookingForPeriod")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public String sendBookingStatisticsForTimePeriod(@RequestParam(name = "in") LocalDate in,
                                                     @RequestParam(name = "out") LocalDate out) {

        return bookingStatistics.sendBookingStatisticsForTimePeriod(in, out);
    }

    @GetMapping("/print")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public String printStatisticsToFile(@RequestParam(name = "in", required = false) LocalDate in,
                                        @RequestParam(name = "out", required = false) LocalDate out) {

        return bookingStatistics.printStatistics(in, out);
    }
}
