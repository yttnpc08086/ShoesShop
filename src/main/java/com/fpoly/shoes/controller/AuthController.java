package com.fpoly.shoes.controller;


import com.fpoly.shoes.model.User;
import com.fpoly.shoes.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(User user) {
        userService.save(user);
        return "redirect:/login";
    }
    @GetMapping("/oauth2/login")
    public String oauth(OAuth2AuthenticationToken oauth2) {
        userService.loginFromOAuth2(oauth2);
        return "forward:/auth/login/success";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }
}