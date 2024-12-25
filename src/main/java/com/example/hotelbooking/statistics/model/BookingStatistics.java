package com.example.hotelbooking.statistics.model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

@Data
@Builder
@Document(collection = "booking")
@NoArgsConstructor
@AllArgsConstructor
public class BookingStatistics {

    @Id
    String id;

    Long useId;

    LocalDate in;

    LocalDate out;
}
