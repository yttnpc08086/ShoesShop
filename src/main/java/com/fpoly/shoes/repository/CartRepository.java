package com.fpoly.shoes.repository;

import com.fpoly.shoes.model.Cart;
import com.fpoly.shoes.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUser(User user);
}