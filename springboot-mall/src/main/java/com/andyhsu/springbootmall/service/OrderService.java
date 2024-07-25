package com.andyhsu.springbootmall.service;

import com.andyhsu.springbootmall.dto.CreateOrderRequest;
import com.andyhsu.springbootmall.dto.OrderQueryParam;
import com.andyhsu.springbootmall.model.Order;

import java.util.List;

public interface OrderService {
    Integer countOrder(OrderQueryParam orderQueryParam);

    List<Order> getOrders(OrderQueryParam orderQueryParam);

    Order getOrderByOrderId(Integer orderId);

    Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest);
}
