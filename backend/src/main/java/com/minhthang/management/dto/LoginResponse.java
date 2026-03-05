package com.minhthang.management.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class LoginResponse {
    private String token;
    private String id;
    private String email;
    private String fullName;
    private String phone;
    private String department;
    private String role;
    private String avatar;
}
