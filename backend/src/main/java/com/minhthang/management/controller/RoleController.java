package com.minhthang.management.controller;

import com.minhthang.management.dto.ApiResponse;
import com.minhthang.management.entity.Role;
import com.minhthang.management.entity.User;
import com.minhthang.management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final UserRepository userRepository;

    private static final Map<Role, String> ROLE_DESCRIPTIONS = Map.of(
            Role.ADMIN, "Quản trị hệ thống",
            Role.DIRECTOR, "Giám đốc",
            Role.SALES_STAFF, "Nhân viên bán hàng",
            Role.SALES_MANAGER, "Quản lý bán hàng",
            Role.WAREHOUSE_MANAGER, "Quản lý kho",
            Role.PRODUCTION_MANAGER, "Quản lý sản xuất"
    );

    @GetMapping
    public ResponseEntity<ApiResponse<List<Map<String, String>>>> getAllRoles() {
        List<Map<String, String>> roles = Arrays.stream(Role.values())
                .map(role -> Map.of(
                        "role", role.name(),
                        "description", ROLE_DESCRIPTIONS.getOrDefault(role, role.name())
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách vai trò thành công", roles));
    }

    @GetMapping("/{role}/users")
    public ResponseEntity<ApiResponse<List<User>>> getUsersByRole(@PathVariable String role) {
        Role roleEnum;
        try {
            roleEnum = Role.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Vai trò không hợp lệ"));
        }
        List<User> users = userRepository.findAll().stream()
                .filter(u -> u.getRole() == roleEnum)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách người dùng theo vai trò thành công", users));
    }
}
