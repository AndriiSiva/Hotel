package com.example.hotelbooking.statistics.listener;

import com.example.hotelbooking.statistics.model.KafkaMessage;
import com.example.hotelbooking.statistics.service.KafkaMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaMessageListener {

   private final KafkaMessageService kafkaMessageService;

    @KafkaListener(topics = "${app.kafka.topicToRead}",
                   groupId = "${app.kafka.kafkaMessageGroupId}",
                   containerFactory = "kafkaMessageConcurrentKafkaListenerContainerFactory")
    public void receive(@Payload KafkaMessage message,
                        @Header(value = KafkaHeaders.RECEIVED_TOPIC) String topic) {

        log.info("Received topic: {}", topic);
        log.info("Received message: {}", message);
        log.info("Message: {}; Topic: {}, Time: {}", message, topic, LocalDateTime.now());

        if (topic.equals("booking-statistics")) {

            System.out.println("topic: " + topic + "message: " + message +
                    " time received in ms:" + LocalDateTime.now());

            kafkaMessageService.saveInDbBookingStatistics(message);
        }

        System.out.println("topic: " + topic + "message: " + message +
                " time received in ms:" + LocalDateTime.now());

        kafkaMessageService.saveInDbUserStatistics(message);
    }

    @KafkaListener(topics = "${app.kafka.topicToWrite}",
            groupId = "${app.kafka.kafkaMessageGroupId}",
            containerFactory = "kafkaMessageConcurrentKafkaListenerContainerFactory")
    public void receiver(@Payload KafkaMessage message,
                        @Header(value = KafkaHeaders.RECEIVED_TOPIC) String topic) {

        log.info("Received topic: {}", topic);
        log.info("Received message: {}", message);
        log.info("Message: {}; Topic: {}, Time: {}", message, topic, LocalDateTime.now());

        if (topic.equals("booking-statistics")) {

            System.out.println("topic: " + topic + "message: " + message +
                    " time received in ms:" + LocalDateTime.now());

            kafkaMessageService.saveInDbBookingStatistics(message);
        }

        System.out.println("topic: " + topic + "message: " + message +
                " time received in ms:" + LocalDateTime.now());

        kafkaMessageService.saveInDbUserStatistics(message);
    }
}
