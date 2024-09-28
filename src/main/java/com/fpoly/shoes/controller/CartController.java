package com.fpoly.shoes.controller;

import com.fpoly.shoes.model.CartItem;
import com.fpoly.shoes.services.CartItemService;
import com.fpoly.shoes.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private OrderService orderService;

    @GetMapping
    public String viewCart(Model model, Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            List<CartItem> cartItems = cartItemService.getCartItems(username);
            model.addAttribute("cartItems", cartItems);

            double totalPrice = cartItems.stream()
                    .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                    .sum();
            model.addAttribute("totalPrice", totalPrice);
        } else {
            model.addAttribute("cartItems", new ArrayList<>());
            model.addAttribute("totalPrice", 0);
        }
        return "cart";
    }

    @PostMapping("/add/{productId}")
    public String addToCart(@PathVariable Long productId, @RequestParam(defaultValue = "1") int quantity, Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            cartItemService.addProductToCart(username, productId, quantity);
        } else {
            return "redirect:/login";
        }
        return "redirect:/cart";
    }

    @PostMapping("/update/{cartItemId}")
    public String updateCartItem(@PathVariable Long cartItemId, @RequestParam int quantity, Principal principal) {
        if (principal != null) {
            cartItemService.updateCartItem(cartItemId, quantity);
        }
        return "redirect:/cart";
    }

    @GetMapping("/remove/{cartItemId}")
    public String removeCartItem(@PathVariable Long cartItemId, Principal principal) {
        if (principal != null) {
            cartItemService.removeCartItem(cartItemId);
        }
        return "redirect:/cart";
    }

    @PostMapping("/checkout")
    public String checkout(Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            orderService.createOrderFromCart(username);
        }
        return "/checkout";
    }
}