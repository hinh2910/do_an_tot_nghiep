package com.minhthang.management.config;

import com.minhthang.management.entity.Role;
import com.minhthang.management.entity.User;
import com.minhthang.management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            createUser("hinht33@gmail.com",       "Hinh123@", "Trần Xuân Hinh",        Role.ADMIN);
            createUser("hinht333@gmail.com",      "Hinh123@", "Ngô Minh Tú",           Role.DIRECTOR);
            createUser("hinht3333@gmail.com",     "Hinh123@", "Đoàn Mạnh Hưng",        Role.SALES_STAFF);
            createUser("hinht33333@gmail.com",    "Hinh123@", "Chạc Trường Lộc",       Role.SALES_MANAGER);
            createUser("hinht333333@gmail.com",   "Hinh123@", "Đức Anh",               Role.WAREHOUSE_MANAGER);
            createUser("hinht3333333@gmail.com",  "Hinh123@", "Hoàng Văn QL Sản Xuất", Role.PRODUCTION_MANAGER);

            log.info("=== Default users created ===");
            log.info("Admin              -> hinht33@gmail.com / Hinh123@  (Trần Xuân Hinh)");
            log.info("Director           -> hinht333@gmail.com / Hinh123@ (Ngô Minh Tú)");
            log.info("Sales Staff        -> hinht3333@gmail.com / Hinh123@ (Đoàn Mạnh Hưng)");
            log.info("Sales Manager      -> hinht33333@gmail.com / Hinh123@ (Chạc Trường Lộc)");
            log.info("Warehouse Manager  -> hinht333333@gmail.com / Hinh123@ (Đức Anh)");
            log.info("Production Manager -> hinht3333333@gmail.com / Hinh123@");
        }
    }

    private void createUser(String email, String password, String fullName, Role role) {
        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .fullName(fullName)
                .role(role)
                .active(true)
                .build();
        userRepository.save(user);
    }
}
