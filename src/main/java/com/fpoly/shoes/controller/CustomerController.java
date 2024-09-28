package com.fpoly.shoes.controller;


import com.fpoly.shoes.model.User;
import com.fpoly.shoes.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/customer")
public class CustomerController {
    private final UserService userService;

    public CustomerController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping({"/", ""})
    public String listUser(Model model) {
        List<User> listUser = userService.listAll();
        model.addAttribute("customers", listUser);
        return "customer/index";
    }

//    @GetMapping("/view/{id}")
//    public String getUserById(@PathVariable Long id, @RequestParam(required = false) String source, Model model) {
//        User user = userService.getUserById(id);
//        if (user != null) {
//            model.addAttribute("customer", user);
//            model.addAttribute("source", source);
//            return "customer/view";
//        } else {
//            return "error/404";
//        }
//    }

    @GetMapping("/create")
    public String showCreateUserForm(Model model) {
        // Khởi tạo customer là một đối tượng User rỗng
        model.addAttribute("customer", new User());
        return "customer/create";
    }

    @PostMapping("/create")
    public String saveUser(@ModelAttribute("customer") User user) {
        // Lưu user và sau đó chuyển hướng về trang customer
        userService.save(user);
        return "redirect:/customer";
    }


    @GetMapping("/update/{id}")
    public String showUpdateUserForm(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id);
        if (user != null) {
            model.addAttribute("customer", user);
            return "customer/update";
        } else {
            return "error/404";
        }
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute User user) {
        User updatedUser = userService.updateUser(user);
        if (updatedUser != null) {
            return "redirect:/customer";
        } else {
            return "error/404";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/customer";
    }
}