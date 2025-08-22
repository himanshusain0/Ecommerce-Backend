package com.ecommerce.ecommerce.service.impl;

import com.ecommerce.ecommerce.domain.OrderStatus;
import com.ecommerce.ecommerce.domain.PaymentStatus;
import com.ecommerce.ecommerce.modal.*;
import com.ecommerce.ecommerce.repository.AddressRepository;
import com.ecommerce.ecommerce.repository.OrderItemRepository;
import com.ecommerce.ecommerce.repository.OrderRepository;
import com.ecommerce.ecommerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private  final OrderItemRepository orderItemRepository;


    @Override
    public Set<Order> createOrder(User user, Address shippingAddress, Cart cart) {
        if(!user.getAddresses().contains(shippingAddress)){
            user.getAddresses().add(shippingAddress);
        }
        Address address = addressRepository.save(shippingAddress);

        Map<Long , List<CartItem>> itemsBySeller = cart.getCartItem().stream()
                .collect(Collectors.groupingBy(item->item.getProduct().getSeller().getId()));
        Set<Order> orders = new HashSet<>();
        for(Map.Entry<Long,List<CartItem>> entry:itemsBySeller.entrySet()){
            Long sellerId = entry.getKey();
            List<CartItem> items = entry.getValue();
            int totalOrderPrice = items.stream().mapToInt(
                    CartItem::getSellingPrice
            ).sum();

            int totalItem = items.stream().mapToInt(CartItem::getQuantity).sum();
            Order createOrder = new Order();
            createOrder.setUser(user);
            createOrder.setSellerId(sellerId);
            createOrder.setTotalMrpPrice(totalOrderPrice);
            createOrder.setTotalSellingPrice(totalOrderPrice);
            createOrder.setTotalItem(totalItem);
            createOrder.setShippingAddress(address);
            createOrder.setOrderStatus(OrderStatus.PENDING);
            createOrder.getPaymentDetails().setStatus(PaymentStatus.PENDING);

            Order savedOrder = orderRepository.save(createOrder);
            orders.add(savedOrder);
            List<OrderItem> orderItem = new ArrayList<>();
            for(CartItem  item : items ){
                OrderItem orderItems= new OrderItem();
                orderItems.setOrder(savedOrder);
                orderItems.setMrpPrice(item.getMrpPrice());
                orderItems.setProduct(item.getProduct());
                orderItems.setQuantity(item.getQuantity());
                orderItems.setSize(item.getSize());
                orderItems.setUserId(item.getUserId());
                orderItems.setSellingPrice(item.getSellingPrice());
                savedOrder.getOderItems().add(orderItems);

                OrderItem createdOrderItem = orderItemRepository.save(orderItems);
                orderItem.add(createdOrderItem);
            }
        }



        return orders;
    }

    @Override
    public Order findOrderById(long id) throws Exception {
        return  orderRepository.findById(id).orElseThrow(()->
                new Exception("order not found"));

    }

    @Override
    public List<Order> userOrderHistory(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public List<Order> sellerOrder(Long sellerId) {
        return orderRepository.findBySellerId(sellerId);

    }

    @Override
    public Order updateOrderStatus(Long orderId, OrderStatus orderStatus) throws Exception {

        Order order = findOrderById(orderId);
        order.setOrderStatus(orderStatus);
        return orderRepository.save(order);
    }

    @Override
    public Order cancelOrder(Long orderId, User user) throws Exception {
        Order order = findOrderById(orderId);
        if(user.getId().equals(order.getUser().getId()) ){
            throw new Exception("You don't have access to this order");
        }

        order.setOrderStatus(OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }

    @Override
    public OrderItem getOrderItemById(Long id) throws Exception {
        return orderItemRepository.findById(id).orElseThrow(()->
                new Exception("orderItem not exist......"));
    }
}
