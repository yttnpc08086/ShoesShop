package com.fpoly.shoes.services;

import com.fpoly.shoes.model.CartItem;

import com.fpoly.shoes.model.Product;
import com.fpoly.shoes.model.User;
import com.fpoly.shoes.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    // Thêm sản phẩm vào giỏ hàng
    public void addProductToCart(String username, Long productId, int quantity) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        CartItem cartItem = cartItemRepository.findByUserAndProductId(user, productId);

        if (cartItem != null) {
            // Nếu sản phẩm đã có trong giỏ hàng, cập nhật số lượng
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        } else {
            // Nếu chưa có, tạo mục giỏ hàng mới
            cartItem = new CartItem();
            cartItem.setUser(user);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
        }

        cartItemRepository.save(cartItem);
    }

    // Lấy danh sách các mục giỏ hàng của người dùng
    public List<CartItem> getCartItems(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        return cartItemRepository.findByUser(user);
    }

    // Cập nhật số lượng của mục giỏ hàng
    public void updateCartItem(Long cartItemId, int quantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("CartItem not found"));
        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);
    }

    // Xóa mục giỏ hàng
    public void removeCartItem(Long cartItemId) {
        if (cartItemRepository.existsById(cartItemId)) {
            cartItemRepository.deleteById(cartItemId);
        }
    }

    // Xóa tất cả mục giỏ hàng của người dùng
    public void clearCart(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        cartItemRepository.deleteAllByUser(user);
    }}