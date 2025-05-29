package knu.oceanbackend.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.servlet.http.HttpServletRequest;
import knu.oceanbackend.dto.user.UserCreateRequestDto;
import knu.oceanbackend.dto.user.UserResponseDto;
import knu.oceanbackend.dto.user.UserUpdateRequestDto;
import knu.oceanbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    @SecurityRequirements
    public ResponseEntity<Void> createUser(@RequestBody UserCreateRequestDto requestDto) {
        userService.createUser(requestDto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<UserResponseDto> getUser(HttpServletRequest request) {
        Long id = (Long) request.getAttribute("user_id");
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PatchMapping
    public ResponseEntity<Void> updateUser(HttpServletRequest request, @RequestBody UserUpdateRequestDto requestDto) {
        Long id = (Long) request.getAttribute("user_id");
        userService.updateUser(id, requestDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(HttpServletRequest request) {
        Long id = (Long) request.getAttribute("user_id");
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
} 