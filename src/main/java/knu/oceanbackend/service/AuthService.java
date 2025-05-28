package knu.oceanbackend.service;

import knu.oceanbackend.dto.auth.AuthRequestDto;
import knu.oceanbackend.dto.auth.AuthResponseDto;
import knu.oceanbackend.entity.User;
import knu.oceanbackend.repository.UserRepository;
import knu.oceanbackend.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public Long validateAndGetUserId(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .map(User::getId)
                .orElse(null);
    }
}
