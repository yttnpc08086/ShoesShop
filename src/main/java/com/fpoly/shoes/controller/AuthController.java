package com.fpoly.shoes.controller;

import com.fpoly.shoes.model.User;
import com.fpoly.shoes.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller // Đánh dấu đây là một Spring controller
public class AuthController {

    @Autowired // Tự động inject UserService
    private UserService userService;

    // Hiển thị form đăng ký
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User()); // Thêm một đối tượng User mới vào model
        return "register"; // Trả về view "register"
    }

    // Xử lý đăng ký người dùng mới
    @PostMapping("/register")
    public String registerUser(User user) {
        userService.save(user); // Lưu người dùng vào cơ sở dữ liệu
        return "redirect:/login"; // Chuyển hướng đến trang đăng nhập sau khi đăng ký thành công
    }

    // Xử lý đăng nhập thông qua OAuth2
    @GetMapping("/oauth2/login")
    public String oauth(OAuth2AuthenticationToken oauth2) {
        userService.loginFromOAuth2(oauth2); // Thực hiện đăng nhập OAuth2
        return "redirect:/login"; // Chuyển hướng sau khi đăng nhập thành công
    }

    // Hiển thị form đăng nhập
    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // Trả về view "login"
    }
}