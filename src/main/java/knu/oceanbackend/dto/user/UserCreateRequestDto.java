package knu.oceanbackend.dto.user;

import lombok.Data;

@Data
public class UserCreateRequestDto {
    private String username;
    private String password;
    private String email;
}
