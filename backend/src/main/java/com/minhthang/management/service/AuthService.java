package com.minhthang.management.service;

import com.minhthang.management.dto.*;
import com.minhthang.management.entity.*;
import com.minhthang.management.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository resetTokenRepository;
    private final LoginLogRepository loginLogRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailService emailService;

    @Value("${app.reset-token.expiry-minutes}")
    private int resetTokenExpiryMinutes;

    @Transactional
    public ApiResponse<LoginResponse> login(LoginRequest request, HttpServletRequest httpRequest) {
        String email = request.getEmail().trim();
        String ip = getClientIp(httpRequest);

        var userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty() || !passwordEncoder.matches(request.getPassword(), userOpt.get().getPassword())) {
            userOpt.ifPresent(u -> saveLoginLog(u, email, false, ip));
            if (userOpt.isEmpty()) saveLoginLog(null, email, false, ip);
            return ApiResponse.error("Tên tài khoản hoặc mật khẩu không phù hợp");
        }

        User user = userOpt.get();

        if (!user.isActive()) {
            return ApiResponse.error("Tài khoản đã bị vô hiệu hóa. Vui lòng liên hệ quản trị viên.");
        }

        String token = jwtService.generateToken(user.getEmail(), user.getRole().name(), request.isRememberMe());

        saveLoginLog(user, email, true, ip);

        LoginResponse response = LoginResponse.builder()
                .token(token)
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .phone(user.getPhone())
                .department(user.getDepartment())
                .role(user.getRole().name())
                .avatar(user.getAvatar())
                .build();

        return ApiResponse.success("Đăng nhập thành công", response);
    }

    @Transactional
    public ApiResponse<Void> forgotPassword(ForgotPasswordRequest request) {
        String email = request.getEmail().trim();
        var userOpt = userRepository.findByEmail(email);

        // Always return success to avoid exposing whether email exists
        if (userOpt.isEmpty()) {
            log.warn("Password reset requested for non-existent email: {}", email);
            return ApiResponse.success("Link reset mật khẩu đã được gửi đến email của bạn");
        }

        User user = userOpt.get();

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusMinutes(resetTokenExpiryMinutes))
                .used(false)
                .build();

        resetTokenRepository.save(resetToken);
        String failedLink = emailService.sendPasswordResetEmail(email, token);

        if (failedLink != null) {
            return ApiResponse.success("Không thể gửi email. Sử dụng link này để đặt lại mật khẩu: " + failedLink);
        }
        return ApiResponse.success("Link reset mật khẩu đã được gửi đến email của bạn");
    }

    @Transactional
    public ApiResponse<Void> resetPassword(ResetPasswordRequest request) {
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            return ApiResponse.error("Mật khẩu xác nhận không trùng khớp");
        }

        var tokenOpt = resetTokenRepository.findByToken(request.getToken());

        if (tokenOpt.isEmpty()) {
            return ApiResponse.error("Token không hợp lệ");
        }

        PasswordResetToken resetToken = tokenOpt.get();

        if (resetToken.isUsed()) {
            return ApiResponse.error("Token đã được sử dụng");
        }

        if (resetToken.isExpired()) {
            return ApiResponse.error("Token đã hết hạn. Vui lòng yêu cầu link mới.");
        }

        User user = resetToken.getUser();

        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            return ApiResponse.error("Mật khẩu mới không được trùng mật khẩu cũ");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        resetToken.setUsed(true);
        resetTokenRepository.save(resetToken);

        return ApiResponse.success("Mật khẩu đã được đặt lại thành công");
    }

    public ApiResponse<Void> validateResetToken(String token) {
        var tokenOpt = resetTokenRepository.findByToken(token);

        if (tokenOpt.isEmpty()) {
            return ApiResponse.error("Token không hợp lệ");
        }

        PasswordResetToken resetToken = tokenOpt.get();

        if (resetToken.isUsed()) {
            return ApiResponse.error("Token đã được sử dụng");
        }

        if (resetToken.isExpired()) {
            return ApiResponse.error("Token đã hết hạn");
        }

        return ApiResponse.success("Token hợp lệ");
    }

    private void saveLoginLog(User user, String email, boolean success, String ip) {
        LoginLog logEntry = LoginLog.builder()
                .user(user)
                .email(email)
                .success(success)
                .ipAddress(ip)
                .build();
        loginLogRepository.save(logEntry);
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
