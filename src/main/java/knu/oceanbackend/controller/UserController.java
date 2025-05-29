package knu.oceanbackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.servlet.http.HttpServletRequest;
import knu.oceanbackend.dto.user.UserRequestDto;
import knu.oceanbackend.dto.user.UserResponseDto;
import knu.oceanbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(
            summary = "사용자 생성",
            description = "username(로그인을 위한 id), password, email을 포함하여 사용자 생성"
    )
    @PostMapping
    @SecurityRequirements
    public ResponseEntity<Void> createUser(@RequestBody UserRequestDto requestDto) {
        userService.createUser(requestDto);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "사용자 조회",
            description = "현재 로그인한 사용자의 정보를 조회"
    )
    @GetMapping
    public ResponseEntity<UserResponseDto> getUser(HttpServletRequest request) {
        Long id = (Long) request.getAttribute("user_id");
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @Operation(
            summary = "사용자 수정",
            description = "현재 로그인한 사용자의 정보를 수정(username, password, email 수정 가능)"
    )
    @PatchMapping
    public ResponseEntity<Void> updateUser(HttpServletRequest request, @RequestBody UserRequestDto requestDto) {
        Long id = (Long) request.getAttribute("user_id");
        userService.updateUser(id, requestDto);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "사용자 삭제",
            description = "현재 로그인한 사용자 삭제"
    )
    @DeleteMapping
    public ResponseEntity<Void> deleteUser(HttpServletRequest request) {
        Long id = (Long) request.getAttribute("user_id");
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
} 