package com.fpoly.shoes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // Đánh dấu đây là một lớp cấu hình Spring
@EnableWebSecurity // Bật tính năng bảo mật web của Spring Security
public class SecurityConfig {

    // Cấu hình bảo mật cho ứng dụng
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize // Cấu hình quyền truy cập cho các request
                        .requestMatchers("/images/**", "/register", "/login", "/css/**", "/js/**", "/",
                                "/category/view/{id}", "/product/view/{id}").permitAll() // Cho phép tất cả truy cập
                        .requestMatchers("/cart/**").hasRole("USER") // Yêu cầu role USER cho các request đến /cart/**
                        .requestMatchers("/admin/**").hasRole("ADMIN") // Yêu cầu role ADMIN cho các request đến /admin/**
                        .anyRequest().authenticated() // Yêu cầu xác thực cho các request còn lại
                )
                .formLogin(form -> form // Cấu hình đăng nhập bằng form
                        .loginPage("/login") // Trang đăng nhập
                        .defaultSuccessUrl("/", true) // Chuyển hướng sau khi đăng nhập thành công
                        .permitAll()
                        .loginProcessingUrl("/login") // URL xử lý đăng nhập
                )
                .rememberMe(r -> r // Cấu hình chức năng "Remember Me"
                        .rememberMeParameter("remember") // Tên tham số trong form
                        .tokenValiditySeconds(86400) // Thời gian sống của token (1 ngày)
                )
                .logout(logout -> logout // Cấu hình đăng xuất
                        .logoutUrl("/logout") // URL đăng xuất
                        .logoutSuccessUrl("/") // Chuyển hướng sau khi đăng xuất thành công
                        .invalidateHttpSession(true) // Hủy phiên làm việc
                        .clearAuthentication(true) // Xóa thông tin xác thực
                        .deleteCookies("JSESSIONID") // Xóa cookie phiên làm việc
                        .permitAll()
                )
                .oauth2Login(o -> o // Cấu hình đăng nhập OAuth2
                                .loginPage("/login")
                                .defaultSuccessUrl("/", true)
                                .failureUrl("/login?error=true")
                                .authorizationEndpoint(a -> a.baseUri("/oauth2/authorization"))
                        // Cấu hình thêm về nhà cung cấp OAuth2 (clientRegistrationRepository)
                )
                .exceptionHandling(handling -> handling // Xử lý ngoại lệ bảo mật
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.sendRedirect("/login"); // Chuyển hướng đến trang đăng nhập nếu chưa xác thực
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {



                            response.sendRedirect("/404"); // Chuyển hướng đến trang 403 nếu không đủ quyền
                        })
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}