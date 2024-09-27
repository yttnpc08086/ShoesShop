package com.fpoly.shoes.services;


import com.fpoly.shoes.model.User;
import com.fpoly.shoes.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private UserDetails toDetails(User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        String role = user.getRole();

        return org.springframework.security.core.userdetails.User
                .withUsername(username)
                .password(password) // Mật khẩu đã được mã hóa
                .roles(role) // Chỉ định các vai trò
                .build();
    }

    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return toDetails(user);
    }


    public void loginFromOAuth2(OAuth2AuthenticationToken oauth2) {
        // Lấy địa chỉ email từ thông tin xác thực OAuth2
        String email = oauth2.getPrincipal().getAttribute("email");

        // Tìm kiếm người dùng dựa trên email, nếu không tìm thấy thì tạo mới
        User user = userRepository.findById(Long.valueOf(email)).orElseGet(() -> {
            User newUser = new User();
            newUser.setUsername(email);         // Đặt tên người dùng là email
            newUser.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
            newUser.setRole("USER");
            userRepository.save(newUser);       // Lưu người dùng mới vào cơ sở dữ liệu
            return newUser;
        });

        // Chuyển đổi người dùng thành UserDetails (đối tượng mà Spring Security hiểu)
        UserDetails userDetails = toDetails(user);

        // Tạo đối tượng xác thực (Authentication) mới
        Authentication auth = new UsernamePasswordAuthenticationToken(
                user,                // Người dùng
                null,                // Mật khẩu (không cần thiết trong trường hợp này)
                userDetails.getAuthorities() // Các quyền của người dùng
        );

        // Đặt đối tượng xác thực vào ngữ cảnh bảo mật (SecurityContext)
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}