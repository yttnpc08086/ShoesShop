package com.fpoly.shoes.repository;

import com.fpoly.shoes.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    // Bạn có thể thêm các phương thức truy vấn tùy chỉnh ở đây nếu cần
    // Ví dụ: tìm tất cả các mục trong một đơn hàng
    List<OrderItem> findByOrderId(Long orderId);
}
