package com.example.hotelbooking.security;

import com.example.hotelbooking.exception.exceptions.RefreshTokenException;
import com.example.hotelbooking.security.jwt.JwtUtils;
import com.example.hotelbooking.user.mapper.UserMapperManual;
import com.example.hotelbooking.user.model.dto.jwt.AuthResponse;
import com.example.hotelbooking.user.model.dto.jwt.LoginRequest;
import com.example.hotelbooking.user.model.dto.jwt.RefreshTokenRequest;
import com.example.hotelbooking.user.model.dto.jwt.RefreshTokenResponse;
import com.example.hotelbooking.user.model.dto.user.RefreshToken;
import com.example.hotelbooking.user.model.dto.user.UserNewDto;
import com.example.hotelbooking.user.model.dto.user.UserResponseDto;
import com.example.hotelbooking.user.model.entity.User;
import com.example.hotelbooking.user.repository.UserRepository;
import com.example.hotelbooking.user.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SecurityService {

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    private final RefreshTokenService refreshTokenService;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public AuthResponse authenticationUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                ));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        RefreshToken refreshToken = refreshTokenService.create(userDetails.getId());

        return AuthResponse.builder()
                .id(userDetails.getId())
                .token(jwtUtils.generateJwtToken(userDetails))
                .refreshToken(refreshToken.getToken())
                .username(userDetails.getUsername())
                .email(userDetails.getEmail())
                .roles(roles)
                .build();
    }

    @Transactional
    public UserResponseDto register(UserNewDto createUserRequest) {
        User user = User.builder()
                .username(createUserRequest.getUsername())
                .email(createUserRequest.getEmail())
                .password(passwordEncoder.encode(createUserRequest.getPassword()))
                .build();

        user.setRoles(createUserRequest.getRoles());
        userRepository.save(user);

        return UserMapperManual.toUserResponseDto(user);
    }

    @Transactional
    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {
        String requestTokenRefresh = request.getRefreshToken();

        return refreshTokenService.getByRefreshToken(requestTokenRefresh)
                .map(refreshTokenService::checkRefreshToken)
                .map(RefreshToken::getId)
                .map(userId -> {
                    User user = userRepository.findById(userId).orElseThrow(() ->
                            new RefreshTokenException("Exception for userId: " + userId));

                    String token = jwtUtils.generateTokenFromUserName(user.getUsername());
                    return new RefreshTokenResponse(token, refreshTokenService.create(userId).getToken());
                }).orElseThrow(() -> new RefreshTokenException("RefreshToken is not found!"));
    }

    @Transactional
    public void logout() {
        var currentPrincipal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (currentPrincipal instanceof AppUserDetails userDetails) {
          refreshTokenService.deleteByUserId(userDetails.getId());
        }
    }
}
