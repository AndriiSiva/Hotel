package com.example.hotelbooking.user.model.dto.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private Long id;

    private String token;

    private String refreshToken;

    private String username;

    private String email;

    private List<String> roles;
}
