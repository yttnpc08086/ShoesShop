package com.fpoly.shoes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/register", "/login", "/css/**", "/js/**","/",
                                "/category/view/{id}","/product/view/{id}").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .rememberMe(r -> r
                        .rememberMeParameter("remember")  // Tham số "Remember Me"
                        .tokenValiditySeconds(86400)  // Cookie tồn tại trong 1 ngày
                        .key("uniqueAndSecretKey")  // Key mã hóa để bảo mật cookie
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                )
                .oauth2Login(o -> o
                    .loginPage("/login") // Trang đăng nhập tùy chỉnh
                        .defaultSuccessUrl("/", true) // URL chuyển hướng sau khi đăng nhập thành công
                                    .failureUrl("/login?error=true")
//                    .failureUrl("/auth/login/error") // URL chuyển hướng nếu đăng nhập thất bại
                    .authorizationEndpoint(a -> a.baseUri("/oauth2/authorization") // Điểm cuối ủy quyền OAuth2
                )
        );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}