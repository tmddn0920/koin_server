package com.K_oin.Koin.Service;

import com.K_oin.Koin.DTO.UserDTO;
import com.K_oin.Koin.Entitiy.User;
import com.K_oin.Koin.EnumData.Nationality;
import com.K_oin.Koin.EnumData.Role;
import com.K_oin.Koin.Repository.UserRepository;
import com.K_oin.Koin.Security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public User registerUser(UserDTO userDTO) {

        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new RuntimeException("이미 존재하는 아이디입니다.");
        }

        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }

        if (userRepository.findByNickname(userDTO.getNickname()).isPresent()) {
            throw new RuntimeException("이미 존재하는 닉네임입니다.");
        }

        User user = User.builder()
                .name(userDTO.getName())
                .username(userDTO.getUsername())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .email(userDTO.getEmail())
                .nickname(userDTO.getNickname())
                .birthDate(userDTO.getBirthDate())
                .nationality(Nationality.valueOf(userDTO.getNationality()))
                .role(Role.USER)
                .createdAt(LocalDateTime.now())
                .build();

        return userRepository.save(user);
    }

    public String login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자 없음"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("비밀번호 불일치");
        }

        return JwtUtil.createToken(username);
    }

    public Optional<UserDTO> getProfile(String username) {
        return userRepository.findByUsername(username)
                .map(user -> UserDTO.builder()
                        .name(user.getName())
                        .email(user.getEmail())
                        .birthDate(user.getBirthDate())
                        .nationality(user.getNationality().name())
                        .nickname(user.getNickname())
                        .build());
    }

    public Optional<Boolean> changePassword(String username, String newPassword) {
        return userRepository.findByUsername(username)
                .map(user -> {
                    // 현재 비밀번호와 새 비밀번호 비교
                    if (passwordEncoder.matches(newPassword, user.getPassword())) {
                        throw new IllegalArgumentException("새 비밀번호가 기존 비밀번호와 같습니다");
                    }

                    // 비밀번호 변경
                    user.setPassword(passwordEncoder.encode(newPassword));
                    userRepository.save(user);
                    return true;
                });
    }

    public boolean isFieldTaken(String type, String value) {
        return switch (type.toLowerCase()) {
            case "nickname" -> userRepository.findByNickname(value).isPresent();
            case "username" -> userRepository.findByUsername(value).isPresent();
            case "email" -> userRepository.findByEmail(value).isPresent();
            default -> throw new IllegalArgumentException("잘못된 타입입니다. username/nickname/email 중 하나여야 합니다.");
        };
    }
}

