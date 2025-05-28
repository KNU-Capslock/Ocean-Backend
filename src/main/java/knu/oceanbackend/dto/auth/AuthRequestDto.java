package knu.oceanbackend.dto.auth;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequestDto {

    private String username;
    private String password;

}
