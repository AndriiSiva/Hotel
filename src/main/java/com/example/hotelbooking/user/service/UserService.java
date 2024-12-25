package com.example.hotelbooking.user.service;

import com.example.hotelbooking.user.model.dto.user.UserNewDto;
import com.example.hotelbooking.user.model.dto.user.UserResponseDto;
import org.springframework.data.domain.PageRequest;
import java.util.List;

public interface UserService {

    List<UserResponseDto> sendAllUserAccountsList(PageRequest page);

    UserResponseDto sendUsersAccountByUserId(Long userId);

    UserResponseDto updateUsersAccountByUserId(Long userId,
                                               UserNewDto updatedUserAccount);

    UserResponseDto deleteUsersAccountByUserId(Long userId);

    UserResponseDto searchUserInDbByUsername(String userName);

    UserResponseDto checkUserNyUserNameAndEmail(String userName,
                                                String email);
}
