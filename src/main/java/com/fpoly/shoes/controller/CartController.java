package com.fpoly.shoes.controller;

import com.fpoly.shoes.model.Cart;
import com.fpoly.shoes.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collections;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public String viewCart(Model model, Principal principal) {
        if (principal != null) {
            Cart cart = cartService.getCartByUsername(principal.getName());
            model.addAttribute("cart", cart);
            model.addAttribute("cartItems", cart.getCartItems());
        } else {
            model.addAttribute("cartItems", Collections.emptyList());
        }
        return "cart";
    }

    @PostMapping("/add")
    @ResponseBody
    public String addToCart(@RequestParam Long productId, @RequestParam int quantity, Principal principal) {
        if (principal == null) {
            return "You must be logged in to add items to your cart.";
        }
        cartService.addProductToCart(principal.getName(), productId, quantity);
        return "Product added to cart";
    }

    @PostMapping("/update")
    @ResponseBody
    public String updateCartItem(@RequestParam Long cartItemId, @RequestParam int quantity, Principal principal) {
        if (principal != null) {
            cartService.updateCartItem(cartItemId, quantity);
            return "Cart item updated";
        } else {
            return "Please login to update cart items";
        }
    }

    @PostMapping("/remove")
    @ResponseBody
    public String removeCartItem(@RequestParam Long cartItemId, Principal principal) {
        if (principal != null) {
            cartService.removeCartItem(cartItemId);
            return "Cart item removed";
        } else {
            return "Please login to remove cart items";
        }
    }
}