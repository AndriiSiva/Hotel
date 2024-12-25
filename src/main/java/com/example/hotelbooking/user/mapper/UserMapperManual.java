package com.example.hotelbooking.user.mapper;

import com.example.hotelbooking.user.model.dto.user.UserResponseDto;
import com.example.hotelbooking.user.model.entity.User;

public class UserMapperManual {

    public static UserResponseDto toUserResponseDto(User user) {

        return UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .email(user.getEmail())
                .build();
    }
}
