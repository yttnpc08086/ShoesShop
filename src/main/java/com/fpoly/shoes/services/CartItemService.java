package com.fpoly.shoes.services;

import com.fpoly.shoes.model.CartItem;
import com.fpoly.shoes.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemService {

    @Autowired
    private CartService cartService;

    public void addProductToCart(String username, Long productId, int quantity) {
        cartService.addProductToCart(username, productId, quantity);
    }

    public List<CartItem> getCartItems(String username) {
        return cartService.getCartByUsername(username).getCartItems();
    }

    public void updateCartItem(Long cartItemId, int quantity) {
        cartService.updateCartItem(cartItemId, quantity);
    }

    public void removeCartItem(Long cartItemId) {
        cartService.removeCartItem(cartItemId);
    }

    public void clearCart(String username) {
        List<CartItem> cartItems = getCartItems(username);
        for (CartItem cartItem : cartItems) {
            removeCartItem(cartItem.getId());
        }
    }
}