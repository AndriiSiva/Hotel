package com.example.hotelbooking.user.controller;

import com.example.hotelbooking.exception.exceptions.AlreadyExistsException;
import com.example.hotelbooking.security.SecurityService;
import com.example.hotelbooking.statistics.model.KafkaMessage;
import com.example.hotelbooking.statistics.service.KafkaMessageService;
import com.example.hotelbooking.user.model.dto.jwt.AuthResponse;
import com.example.hotelbooking.user.model.dto.jwt.LoginRequest;
import com.example.hotelbooking.user.model.dto.jwt.RefreshTokenRequest;
import com.example.hotelbooking.user.model.dto.jwt.RefreshTokenResponse;
import com.example.hotelbooking.user.model.dto.user.UserNewDto;
import com.example.hotelbooking.user.model.dto.user.UserResponseDto;
import com.example.hotelbooking.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    @Value("${app.kafka.topicToRead}")
    private String topic1;

    private final KafkaMessageService kafkaMessageService;

    private final UserRepository userRepository;

    private final SecurityService securityService;

    @PostMapping("/signing")
    @ResponseStatus(HttpStatus.OK)
    public AuthResponse authUser(@RequestBody LoginRequest loginRequest) {

        log.info("\nLog-in in AuthController was successes with username: %s"
                .formatted(loginRequest.getUsername())
                + " at time: " + LocalDateTime.now() + "\n");

        return securityService.authenticationUser(loginRequest);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto registerUser(@RequestBody UserNewDto request) {
        if (userRepository.existsByUsername(request.getUsername()) &&
                userRepository.existsByEmail(request.getEmail())) {
            throw new AlreadyExistsException("Email: %s or Username: %s already taken! at time "
                    .formatted(request.getUsername(), request.getEmail())
                    + LocalDateTime.now());

        }

        UserResponseDto userResponseDto = securityService.register(request);

        KafkaMessage message = new KafkaMessage();

        message.setType("user-statistics");
        message.setMessage(new ArrayList<>(List.of(userResponseDto.getId().toString())));
        kafkaMessageService.saveInDbUserStatistics(message);

        log.info("\nUser registration in AuthController was successes with username: %s"
                .formatted(request.getUsername())
                + " at time: " + LocalDateTime.now() + "\n");

        return userResponseDto;
    }

    @PostMapping("/refresh-token")
    @ResponseStatus(HttpStatus.OK)
    public RefreshTokenResponse refreshToken(@RequestBody RefreshTokenRequest request) {

        log.info("\nUser token in AuthController was successfully refreshed with request token: %s"
                .formatted(request.getRefreshToken())
                + " at time: " + LocalDateTime.now() + "\n");

        return securityService.refreshToken(request);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public String logoutUser(@AuthenticationPrincipal UserDetails details) {
        securityService.logout();

        log.info("\nUser with username: %s was successfully logout in AuthController!"
                .formatted(details.getUsername())
                + " at time: " + LocalDateTime.now() + "\n");

        return "User was logout! Username is: "
                + details.getUsername()
                + " at time "
                + LocalDateTime.now();
    }
}
