package com.fpoly.shoes.services;

import com.fpoly.shoes.model.Cart;
import com.fpoly.shoes.model.CartItem;
import com.fpoly.shoes.model.Product;
import com.fpoly.shoes.model.User;
import com.fpoly.shoes.repository.CartItemRepository;
import com.fpoly.shoes.repository.CartRepository;
import com.fpoly.shoes.repository.ProductRepository;
import com.fpoly.shoes.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    public Cart getCartByUsername(String username) {
        User user = userRepository.findByUsername(username);
        return cartRepository.findByUser(user);
    }

    public void addProductToCart(String username, Long productId, int quantity) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        Cart cart = cartRepository.findByUser(user);
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cartRepository.save(cart);
        }

        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product);
        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        } else {
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
        }

        cartItemRepository.save(cartItem);
    }

    public void updateCartItem(Long cartItemId, int quantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("CartItem not found"));
        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);
    }

    public void removeCartItem(Long cartItemId) {
        if (cartItemRepository.existsById(cartItemId)) {
            cartItemRepository.deleteById(cartItemId);
        }
    }

    public void clearCart(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        Cart cart = cartRepository.findByUser(user);
        if (cart != null) {
            List<CartItem> cartItems = cart.getCartItems();
            cartItemRepository.deleteAll(cartItems);
        }
    }
}