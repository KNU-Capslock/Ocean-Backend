package knu.oceanbackend.controller;

import jakarta.servlet.http.HttpServletRequest;
import knu.oceanbackend.dto.user.UserResponseDto;
import knu.oceanbackend.dto.user.UserUpdateRequestDto;
import knu.oceanbackend.entity.User;
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
    public ResponseEntity<Void> createUser(@RequestBody User user) {
        userService.createUser(user);
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