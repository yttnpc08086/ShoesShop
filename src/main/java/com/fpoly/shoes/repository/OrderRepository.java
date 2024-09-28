package com.fpoly.shoes.repository;

import com.fpoly.shoes.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    // Bạn có thể thêm các phương thức truy vấn tùy chỉnh ở đây nếu cần
    // Ví dụ: tìm tất cả các đơn hàng của một người dùng
    List<Order> findByUserId(Long userId);
}
