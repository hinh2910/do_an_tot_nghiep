package com.minhthang.management.service;

import com.minhthang.management.dto.ApiResponse;
import com.minhthang.management.entity.Role;
import com.minhthang.management.entity.User;
import com.minhthang.management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ApiResponse<List<User>> getAll(String q, String role, String status) {
        List<User> users;
        if (q != null && !q.isBlank()) {
            users = userRepository.search(q.trim());
        } else {
            users = userRepository.findAll();
        }
        if (role != null && !role.isBlank()) {
            try {
                Role r = Role.valueOf(role);
                users = users.stream().filter(u -> u.getRole() == r).toList();
            } catch (Exception ignored) {}
        }
        if ("active".equalsIgnoreCase(status)) {
            users = users.stream().filter(User::isActive).toList();
        } else if ("inactive".equalsIgnoreCase(status)) {
            users = users.stream().filter(u -> !u.isActive()).toList();
        }
        return ApiResponse.success("Lấy danh sách người dùng thành công", users);
    }

    public ApiResponse<User> getById(String id) {
        return userRepository.findById(id)
                .map(u -> ApiResponse.success("Thành công", u))
                .orElse(ApiResponse.error("Người dùng không tồn tại"));
    }

    @Transactional
    public ApiResponse<User> create(User req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            return ApiResponse.error("Email đã tồn tại trong hệ thống");
        }
        User user = User.builder()
                .email(req.getEmail().trim())
                .password(passwordEncoder.encode(req.getPassword()))
                .fullName(req.getFullName())
                .phone(req.getPhone())
                .department(req.getDepartment())
                .role(req.getRole() != null ? req.getRole() : Role.SALES_STAFF)
                .active(true)
                .build();
        return ApiResponse.success("Tạo người dùng thành công", userRepository.save(user));
    }

    @Transactional
    public ApiResponse<User> update(String id, User req) {
        var opt = userRepository.findById(id);
        if (opt.isEmpty()) return ApiResponse.error("Người dùng không tồn tại");

        User user = opt.get();
        if (req.getFullName() != null) user.setFullName(req.getFullName());
        if (req.getPhone() != null) user.setPhone(req.getPhone());
        if (req.getDepartment() != null) user.setDepartment(req.getDepartment());
        if (req.getRole() != null) user.setRole(req.getRole());
        if (req.getEmail() != null && !req.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(req.getEmail())) {
                return ApiResponse.error("Email đã tồn tại");
            }
            user.setEmail(req.getEmail());
        }
        return ApiResponse.success("Cập nhật thành công", userRepository.save(user));
    }

    @Transactional
    public ApiResponse<Void> toggleActive(String id) {
        var opt = userRepository.findById(id);
        if (opt.isEmpty()) return ApiResponse.error("Người dùng không tồn tại");
        User user = opt.get();
        if (user.getRole() == Role.ADMIN && !user.isActive()) {
            return ApiResponse.error("Không thể vô hiệu hóa Admin");
        }
        user.setActive(!user.isActive());
        userRepository.save(user);
        return ApiResponse.success(user.isActive() ? "Đã kích hoạt tài khoản" : "Đã vô hiệu hóa tài khoản");
    }

    @Transactional
    public ApiResponse<Void> resetPassword(String id) {
        var opt = userRepository.findById(id);
        if (opt.isEmpty()) return ApiResponse.error("Người dùng không tồn tại");
        User user = opt.get();
        String tempPassword = "Reset@" + UUID.randomUUID().toString().substring(0, 6);
        user.setPassword(passwordEncoder.encode(tempPassword));
        userRepository.save(user);
        return ApiResponse.success("Mật khẩu đã được reset thành: " + tempPassword);
    }

    @Transactional
    public ApiResponse<User> updateProfile(String email, String fullName, String phone) {
        var opt = userRepository.findByEmail(email);
        if (opt.isEmpty()) return ApiResponse.error("Không tìm thấy người dùng");
        User user = opt.get();
        if (fullName != null) user.setFullName(fullName);
        if (phone != null) user.setPhone(phone);
        return ApiResponse.success("Cập nhật hồ sơ thành công", userRepository.save(user));
    }

    @Transactional
    public ApiResponse<Void> changePassword(String email, String currentPassword, String newPassword) {
        var opt = userRepository.findByEmail(email);
        if (opt.isEmpty()) return ApiResponse.error("Không tìm thấy người dùng");
        User user = opt.get();
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            return ApiResponse.error("Mật khẩu hiện tại không đúng");
        }
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            return ApiResponse.error("Mật khẩu mới không được trùng mật khẩu cũ");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return ApiResponse.success("Đổi mật khẩu thành công. Vui lòng đăng nhập lại.");
    }
}
