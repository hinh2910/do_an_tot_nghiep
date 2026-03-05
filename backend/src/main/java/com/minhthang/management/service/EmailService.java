package com.minhthang.management.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    public String sendPasswordResetEmail(String toEmail, String token) {
        String resetLink = frontendUrl + "/reset-password?token=" + token;

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Minh Thang - Đặt lại mật khẩu");
            message.setText(
                "Xin chào,\n\n" +
                "Bạn đã yêu cầu đặt lại mật khẩu cho tài khoản của mình.\n\n" +
                "Vui lòng nhấn vào link bên dưới để đặt lại mật khẩu:\n" +
                resetLink + "\n\n" +
                "Link này sẽ hết hạn sau 30 phút.\n\n" +
                "Nếu bạn không yêu cầu đặt lại mật khẩu, vui lòng bỏ qua email này.\n\n" +
                "Trân trọng,\nMinh Thang Management System"
            );
            mailSender.send(message);
            log.info("Password reset email sent to: {}", toEmail);
            return null;
        } catch (Exception e) {
            log.warn("Email sending failed for {}. Returning reset link directly.", toEmail);
            return resetLink;
        }
    }
}
