package knu.oceanbackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import knu.oceanbackend.dto.auth.AuthRequestDto;
import knu.oceanbackend.dto.auth.AuthResponseDto;
import knu.oceanbackend.security.jwt.JwtTokenProvider;
import knu.oceanbackend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthService authService;

    @Operation(
            summary = "로그인",
            description = "username, password 입력 후 jwt 발급"
    )
    @PostMapping("/login")
    @SecurityRequirements
    public ResponseEntity<AuthResponseDto> login(@RequestBody AuthRequestDto request) {

        Long userId = authService.validateAndGetUserId(request.getUsername(), request.getPassword());
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // JWT 생성 (storeId 포함)
        String token = jwtTokenProvider.generateToken(request.getUsername(), userId);
        return ResponseEntity.ok(new AuthResponseDto(token));
    }

}