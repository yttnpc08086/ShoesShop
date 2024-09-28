package com.fpoly.shoes.services;

import com.fpoly.shoes.model.CartItem;
import com.fpoly.shoes.model.Order;
import com.fpoly.shoes.model.OrderItem;
import com.fpoly.shoes.model.User;
import com.fpoly.shoes.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void createOrderFromCart(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return; // Hoặc xử lý trường hợp không tìm thấy người dùng
        }

        List<CartItem> cartItems = cartItemRepository.findByUser(user); // Sửa đổi tại đây

        if (cartItems.isEmpty()) {
            return; // Hoặc xử lý trường hợp giỏ hàng trống
        }

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(new Date());
        order.setStatus("PENDING"); // Trạng thái đơn hàng ban đầu

        double totalPrice = 0;
        List<OrderItem> orderItems = new ArrayList<>();

        // Tạo OrderItem cho từng CartItem
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProduct().getPrice());
            orderItems.add(orderItem);
            totalPrice += orderItem.getPrice() * orderItem.getQuantity();
        }

        order.setTotalPrice(totalPrice);
        order.setOrderItems(orderItems);

        orderRepository.save(order); // Lưu đơn hàng
        orderItemRepository.saveAll(orderItems); // Lưu tất cả các OrderItem
        cartItemRepository.deleteAllByUser(user); // Sửa đổi tại đây
    }
}