package com.example.hotelbooking.user.model.dto.user;

import com.example.hotelbooking.user.enums.RoleType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserNewDto {

    @Size(max=32)
    @NotBlank
    private String username;

    @Size(max=32)
    @Email
    @NotBlank
    private String email;

    @Size(max=32)
    @NotBlank
    private String password;

    @NotBlank
    private List<RoleType> roles = new ArrayList<>();
}
