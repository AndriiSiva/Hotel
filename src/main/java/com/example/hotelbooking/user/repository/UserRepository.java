package com.example.hotelbooking.user.repository;

import com.example.hotelbooking.user.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = """
            SELECT *
            FROM users
            WHERE username LIKE :userName
            """, nativeQuery = true)
    Optional<User> searchNyUsername(@Param("userName") String userName);

    @Query(value = """
            SELECT *
            FROM users
            WHERE username LIKE :userName
            AND email LIKE :email
            """, nativeQuery = true)
    Optional<User> checkByUserNameAndEmail(@Param("userName") String userName,
                                           @Param("email") String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<User> findByUsername(String username);
}
