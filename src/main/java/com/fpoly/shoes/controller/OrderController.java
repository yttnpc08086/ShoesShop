package com.fpoly.shoes.controller;

import com.fpoly.shoes.model.Order;
import com.fpoly.shoes.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/checkout")
    @ResponseBody
    public String checkout(Principal principal) {
        if (principal != null) {
            Order order = orderService.createOrder(principal.getName());
            return "Order created with ID: " + order.getId();
        } else {
            return "Please login to checkout";
        }
    }
}