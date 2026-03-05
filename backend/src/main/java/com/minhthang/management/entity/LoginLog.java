package com.minhthang.management.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "login_logs")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class LoginLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 100)
    private String email;

    private boolean success;

    @Column(name = "ip_address", length = 50)
    private String ipAddress;

    @Column(name = "login_at", nullable = false)
    private LocalDateTime loginAt;

    @PrePersist
    protected void onCreate() {
        loginAt = LocalDateTime.now();
    }
}
