package com.fpoly.shoes.services;

import com.fpoly.shoes.model.*;
import com.fpoly.shoes.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private UserRepository userRepository;

    public Order createOrder(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        Cart cart = cartService.getCartByUsername(username);
        if (cart == null || cart.getCartItems().isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }

        Order order = new Order();
        order.setUser(user);
        order.setTotalAmount(cart.getCartItems().stream().mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity()).sum());
        orderRepository.save(order);

        for (CartItem cartItem : cart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProduct().getPrice());
            orderItemRepository.save(orderItem);
        }

        cartService.clearCart(username);
        return order;
    }
}