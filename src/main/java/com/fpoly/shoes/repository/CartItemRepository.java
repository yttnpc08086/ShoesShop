package com.fpoly.shoes.repository;

import com.fpoly.shoes.model.CartItem;
import com.fpoly.shoes.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // Tìm các mục giỏ hàng theo User entity thay vì dùng username trực tiếp
    List<CartItem> findByUser(User user);

    // Tìm kiếm một mục giỏ hàng theo User entity và Product ID
    CartItem findByUserAndProductId(User user, Long productId);

    // Xóa tất cả các mục giỏ hàng của một User dựa trên User entity
    void deleteAllByUser(User user);
}
