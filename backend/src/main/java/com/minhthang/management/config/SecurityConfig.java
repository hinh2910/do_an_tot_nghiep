package com.minhthang.management.config;

import com.minhthang.management.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()

                // User management - Admin only
                .requestMatchers("/api/users/**").hasRole("ADMIN")

                // Roles - Admin full, Director view
                .requestMatchers(HttpMethod.GET, "/api/roles/**").hasAnyRole("ADMIN", "DIRECTOR")
                .requestMatchers("/api/roles/**").hasRole("ADMIN")

                // Approvals - Admin & Director
                .requestMatchers("/api/approvals/**").hasAnyRole("ADMIN", "DIRECTOR")

                // Customers - Sales roles + Admin
                .requestMatchers("/api/customers/**").hasAnyRole("ADMIN", "SALES_STAFF", "SALES_MANAGER", "DIRECTOR")

                // Quotations - Sales roles + Admin
                .requestMatchers("/api/quotations/**").hasAnyRole("ADMIN", "SALES_STAFF", "SALES_MANAGER", "DIRECTOR")

                // Sales Orders - Sales roles + Admin + Production (view)
                .requestMatchers(HttpMethod.GET, "/api/sales-orders/**").hasAnyRole("ADMIN", "SALES_STAFF", "SALES_MANAGER", "DIRECTOR", "PRODUCTION_MANAGER", "WAREHOUSE_MANAGER")
                .requestMatchers("/api/sales-orders/**").hasAnyRole("ADMIN", "SALES_STAFF", "SALES_MANAGER")

                // Products - All can view, Admin + Sales can modify
                .requestMatchers(HttpMethod.GET, "/api/products/**").authenticated()
                .requestMatchers("/api/products/**").hasAnyRole("ADMIN", "SALES_STAFF", "SALES_MANAGER")

                // Suppliers - Admin + Warehouse Manager
                .requestMatchers(HttpMethod.GET, "/api/suppliers/**").authenticated()
                .requestMatchers("/api/suppliers/**").hasAnyRole("ADMIN", "WAREHOUSE_MANAGER")

                // Warehouses - Admin + Warehouse Manager
                .requestMatchers(HttpMethod.GET, "/api/warehouses/**").authenticated()
                .requestMatchers("/api/warehouses/**").hasAnyRole("ADMIN", "WAREHOUSE_MANAGER")

                // Materials - Admin + Warehouse + Production
                .requestMatchers(HttpMethod.GET, "/api/materials/**").authenticated()
                .requestMatchers("/api/materials/**").hasAnyRole("ADMIN", "WAREHOUSE_MANAGER", "PRODUCTION_MANAGER")

                // Stock operations - Admin + Warehouse Manager
                .requestMatchers("/api/stock/**").hasAnyRole("ADMIN", "WAREHOUSE_MANAGER")
                .requestMatchers("/api/stock-counts/**").hasAnyRole("ADMIN", "WAREHOUSE_MANAGER")

                // BOM - Admin + Production Manager
                .requestMatchers(HttpMethod.GET, "/api/boms/**").hasAnyRole("ADMIN", "PRODUCTION_MANAGER", "DIRECTOR", "WAREHOUSE_MANAGER")
                .requestMatchers("/api/boms/**").hasAnyRole("ADMIN", "PRODUCTION_MANAGER")

                // Production Orders - Admin + Production Manager
                .requestMatchers(HttpMethod.GET, "/api/production-orders/**").hasAnyRole("ADMIN", "PRODUCTION_MANAGER", "DIRECTOR", "WAREHOUSE_MANAGER")
                .requestMatchers("/api/production-orders/**").hasAnyRole("ADMIN", "PRODUCTION_MANAGER")

                // Work Orders - Admin + Production Manager
                .requestMatchers(HttpMethod.GET, "/api/work-orders/**").hasAnyRole("ADMIN", "PRODUCTION_MANAGER", "DIRECTOR")
                .requestMatchers("/api/work-orders/**").hasAnyRole("ADMIN", "PRODUCTION_MANAGER")

                // Dashboard & Profile - All authenticated
                .requestMatchers("/api/dashboard/**").authenticated()
                .requestMatchers("/api/profile/**").authenticated()

                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(frontendUrl));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
