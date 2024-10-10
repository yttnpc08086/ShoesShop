package com.fpoly.shoes.repository;

import com.fpoly.shoes.model.Cart;
import com.fpoly.shoes.model.CartItem;
import com.fpoly.shoes.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByCartAndProduct(Cart cart, Product product);
    List<CartItem> findByCart(Cart cart);
    void deleteAllByCart(Cart cart);
}