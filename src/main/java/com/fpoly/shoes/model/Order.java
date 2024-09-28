package com.fpoly.shoes.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Temporal(TemporalType.TIMESTAMP)
    // Lưu trữ cả ngày và giờ
    private java.util.Date orderDate;

    private double totalPrice;

    private String status; // Ví dụ: "PENDING", "PROCESSING", "COMPLETED", "CANCELLED"

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL) // Liên kết một chiều với OrderItem
    private List<OrderItem> orderItems;
}