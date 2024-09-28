package com.fpoly.shoes.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    @GetMapping("/404")
    public String handle404() {
        return "404"; // Trả về tên view 404.html
    }
}
