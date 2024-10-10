package com.fpoly.shoes.repository;

import com.fpoly.shoes.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}