package com.minhthang.management.controller;

import com.minhthang.management.dto.ApiResponse;
import com.minhthang.management.entity.User;
import com.minhthang.management.repository.UserRepository;
import com.minhthang.management.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserRepository userRepository;
    private final UserService userService;

    private String getCurrentEmail() {
        return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @GetMapping
    public ResponseEntity<ApiResponse<User>> getProfile() {
        String email = getCurrentEmail();
        return userRepository.findByEmail(email)
                .map(u -> ResponseEntity.ok(ApiResponse.success("Thành công", u)))
                .orElse(ResponseEntity.badRequest().body(ApiResponse.error("Không tìm thấy người dùng")));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<User>> updateProfile(@RequestBody Map<String, String> body) {
        String email = getCurrentEmail();
        ApiResponse<User> res = userService.updateProfile(email, body.get("fullName"), body.get("phone"));
        return res.isSuccess() ? ResponseEntity.ok(res) : ResponseEntity.badRequest().body(res);
    }

    @PutMapping("/password")
    public ResponseEntity<ApiResponse<Void>> changePassword(@RequestBody Map<String, String> body) {
        String email = getCurrentEmail();
        String currentPassword = body.get("currentPassword");
        String newPassword = body.get("newPassword");

        if (newPassword == null || newPassword.length() < 8) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Mật khẩu mới phải có ít nhất 8 ký tự"));
        }
        if (!newPassword.matches(".*[A-Z].*") || !newPassword.matches(".*[a-z].*") || !newPassword.matches(".*\\d.*")) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Mật khẩu phải có chữ hoa, chữ thường và số"));
        }

        ApiResponse<Void> res = userService.changePassword(email, currentPassword, newPassword);
        return res.isSuccess() ? ResponseEntity.ok(res) : ResponseEntity.badRequest().body(res);
    }
}
