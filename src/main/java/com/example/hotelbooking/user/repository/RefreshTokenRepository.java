package com.example.hotelbooking.user.repository;

import com.example.hotelbooking.user.model.dto.user.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    void deleteByUserId(Long UserId);
}
