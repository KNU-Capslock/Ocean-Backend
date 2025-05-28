package knu.oceanbackend.service;

import knu.oceanbackend.dto.user.UserResponseDto;
import knu.oceanbackend.dto.user.UserUpdateRequestDto;
import knu.oceanbackend.entity.User;
import knu.oceanbackend.exception.UserNotFoundException;
import knu.oceanbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void createUser(User user) {

        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);
    }

    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return UserResponseDto.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }

    @Transactional
    public void updateUser(Long id, UserUpdateRequestDto requestDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        if (requestDto.getUsername() != null) user.setUsername(requestDto.getUsername());
        if (requestDto.getEmail() != null) user.setEmail(requestDto.getEmail());
        if (requestDto.getPassword() != null) user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
} 