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

import java.util.*;

@Service // Đánh dấu đây là một Spring service
public class UserService implements UserDetailsService { // Cung cấp dịch vụ load thông tin người dùng cho Spring Security

    @Autowired // Tự động inject UserRepository
    private UserRepository userRepository;

    @Autowired // Tự động inject PasswordEncoder
    private PasswordEncoder passwordEncoder;

    // Chuyển đổi đối tượng User thành UserDetails để Spring Security sử dụng
    private UserDetails toDetails(User user) {
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword()) // Mật khẩu đã được mã hóa
                .roles(user.getRole()) // Chỉ định các vai trò
                .build();
    }

    private boolean isUserAdmin(User user) {
        return "ADMIN".equalsIgnoreCase(user.getRole());
    }

    // Lưu người dùng vào cơ sở dữ liệu, mã hóa mật khẩu và gán role
    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Mã hóa mật khẩu
        user.setRole(isUserAdmin(user) ? "ADMIN" : "USER"); // Gán role dựa trên kết quả kiểm tra
        userRepository.save(user);
    }

    public List<User> listAll() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User updateUser(User user) {
        User existingUser = userRepository.findById(user.getId()).orElse(null);
        if (existingUser != null) {
            existingUser.setFullname(user.getFullname());
            existingUser.setUsername(user.getUsername());
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
            existingUser.setEmail(user.getEmail());
            existingUser.setAddress(user.getAddress());
            existingUser.setPhone(user.getPhone());
            existingUser.setRole(user.getRole());
            return userRepository.save(existingUser);
        } else {
            return null;
        }
    }

    // Triển khai phương thức loadUserByUsername từ UserDetailsService
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username); // Tìm kiếm người dùng theo username
        if (user == null) {
            throw new UsernameNotFoundException("User not found"); // Nếu không tìm thấy, ném ra ngoại lệ
        }
        return toDetails(user); // Chuyển đổi và trả về UserDetails
    }

    public void loginFromOAuth2(OAuth2AuthenticationToken oauth2) {
        String email = oauth2.getPrincipal().getAttribute("email");
        System.out.println("OAuth2 login initiated with email: " + email);

        Optional<User> optionalUser = userRepository.findByEmail(email);
        User user = optionalUser.orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setUsername(email);
            newUser.setPassword(passwordEncoder.encode(UUID.randomUUID().toString())); // Random password for OAuth2 users
            newUser.setRole("USER");
            System.out.println("Saving new user to the database: " + newUser);
            return userRepository.save(newUser); // Save the new user to the DB
        });

        // Ensure the user is saved and present in the database
        if (user.getId() == null) {
            throw new RuntimeException("User creation failed.");
        }

        // Authenticate the user in the Spring Security context
        UserDetails userDetails = toDetails(user);
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        System.out.println("User logged in successfully: " + user.getUsername());
    }

}