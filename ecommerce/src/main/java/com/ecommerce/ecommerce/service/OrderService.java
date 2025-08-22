package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.domain.OrderStatus;
import com.ecommerce.ecommerce.modal.*;

import java.util.List;
import java.util.Set;

public interface OrderService {

    Set<Order> createOrder(User user , Address shippingAddress , Cart cart);
    Order findOrderById(long id) throws Exception;
    List<Order> userOrderHistory(Long userId);
    List<Order> sellerOrder(Long sellerId);
    Order updateOrderStatus(Long orderId, OrderStatus orderStatus) throws Exception;
    Order cancelOrder (Long orderId,User user) throws Exception;
    OrderItem getOrderItemById(Long id) throws Exception;
}
