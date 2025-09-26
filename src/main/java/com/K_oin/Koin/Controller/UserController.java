package com.K_oin.Koin.Controller;

import com.K_oin.Koin.DTO.ApiResponse;
import com.K_oin.Koin.DTO.UserDTO;
import com.K_oin.Koin.Entitiy.User;
import com.K_oin.Koin.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/management/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/Register")
    public ResponseEntity<ApiResponse<String>> RegisterUser(@RequestBody UserDTO userDTO){
        try{
            User user = userService.registerUser(userDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, user.getUsername(), "회원가입 성공"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, null, e.getMessage()));
        }
    }

    @GetMapping("/MyProfile")
    public ResponseEntity<?> getMyProfile(Authentication authentication){
        Optional<UserDTO> optionalUser = userService.getProfile(authentication.getName());

        return optionalUser
                .map(ResponseEntity::ok) // 존재하면 200 OK + DTO 반환
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body((UserDTO) Map.of(
                                "success", false,
                                "message", "user가 존재하지 않는 토큰입니다")));
    }

    @PostMapping("/ChangePassword")
    public ResponseEntity<?> changePassword(@RequestBody UserDTO userDTO, Authentication authentication){
        try {
            Optional<Boolean> result = userService.changePassword(authentication.getName(), userDTO.getPassword());
            if (result.isPresent() && result.get()) {
                return ResponseEntity.ok(Map.of("success", true));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", e.getMessage()));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("success", false, "message", "비밀번호 변경 실패"));
    }

    @GetMapping("/RegisterValidation")
    public ResponseEntity<ApiResponse<String>> checkField(
            @RequestParam String type,
            @RequestParam String value) {

        boolean exists;

        try {
            exists = userService.isFieldTaken(type, value);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, null, e.getMessage()));
        }

        String fieldName;
        switch (type.toLowerCase()) {
            case "nickname" -> fieldName = "닉네임";
            case "username" -> fieldName = "아이디";
            case "email" -> fieldName = "이메일";
            default -> throw new IllegalArgumentException("잘못된 타입입니다. username/nickname/email 중 하나여야 합니다.");
        }

        String message = exists ? "이미 존재하는 " + fieldName + "입니다."
                : "사용 가능한 " + fieldName + "입니다.";

        return ResponseEntity.ok(new ApiResponse<>(!exists, value, message));
    }
}
